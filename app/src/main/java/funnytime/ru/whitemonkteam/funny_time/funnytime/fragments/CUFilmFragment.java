package funnytime.ru.whitemonkteam.funny_time.funnytime.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileNotFoundException;
import java.util.Calendar;

import funnytime.ru.whitemonkteam.funny_time.funnytime.ImageDownloadActivity;
import funnytime.ru.whitemonkteam.funny_time.funnytime.R;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.BitmapUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.HelpUtils;

public class CUFilmFragment extends Fragment
{
	
	EditText editName;
	Spinner spinner;
	RatingBar rb;
    CheckBox cbPrivate;
    CheckBox cbTime;
    ImageView iv;
	
	Button btnSave, btnCancel, btnRemember, btnComment;

    boolean showCb = true;
	
	
	Film item = null;
	int action = -1;

    long dateRemember = 0;
    long dateSaw = 0;

    String imagePath = "";
    String imageUrl = "";
    String comment = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.cu_film_layout, container, false);
		
		editName = (EditText) view.findViewById(R.id.filmEditName);
		spinner = (Spinner) view.findViewById(R.id.filmSpinner);
		rb = (RatingBar) view.findViewById(R.id.filmRatingBar);
        iv = (ImageView) view.findViewById(R.id.film_iv);

        cbPrivate = (CheckBox) view.findViewById(R.id.filmCbShowAll);
        cbTime = (CheckBox) view.findViewById(R.id.filmCbToday);
        cbPrivate.setChecked(true);
        cbTime.setChecked(true);
		
		
		btnSave = (Button) view.findViewById(R.id.filmBtnOk);
		btnCancel = (Button) view.findViewById(R.id.filmBtnCancel);
        btnComment = (Button) view.findViewById(R.id.filmBtnCall);
		
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
				getActivity().getResources().getStringArray(R.array.strArrayFilm));
		spinner.setAdapter(spinAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				
				if ( position == 0)
				{
					action = Constants.TYPE_WANT_TO_SEE;
					rb.setVisibility(View.GONE);
                    cbTime.setVisibility(View.GONE);
					btnRemember.setVisibility(View.VISIBLE);
                    btnComment.setVisibility(View.GONE);
				}
				else
				{
                    action = Constants.TYPE_SAW;
					rb.setVisibility(View.VISIBLE);
                    cbTime.setVisibility(View.VISIBLE);
					btnRemember.setVisibility(View.GONE);
                    btnComment.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		if (getArguments() != null)
		{
	       item = (Film) getArguments().getSerializable(Constants.EXTRA_ITEM);
	       
	     //  getActivity().setTitle(item.Name);
		}
		else
		{
			 //getActivity().setTitle("");
		}
		
		
		if (item != null)
		{
			editName.setText(item.Name);
			rb.setRating(item.Mark);
            cbPrivate.setChecked(!item.IsPrivate);
            action = item.Action;

            imagePath = item.ImagePath;
            imageUrl = item.ImageURL;
            showImage(item.ImagePath);

            if ( action ==  Constants.TYPE_WANT_TO_SEE)
            {
                dateRemember = item.DateRemember;
                spinner.setSelection(0);
            }
            else if ( action ==  Constants.TYPE_SAW || action ==  Constants.TYPE_DO_NOT_LIKE)
            {
                dateSaw = item.DateTime;
                if ( item.IsSawToday && HelpUtils.isToday(dateSaw))
                {
                    cbTime.setChecked(true);
                }
                else
                {
                    cbTime.setChecked(false);

                    cbTime.setText(HelpUtils.getTimeString(item.DateTime));

                    showCb = false;
                }



                spinner.setSelection(1);
            }
		}
		else
		{
			spinner.setSelection(0);
			rb.setRating(0);
		}
		
		btnSave.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) 
			{
				final Film film = new Film();
				film.Name = editName.getText().toString();
				film.Mark = rb.getRating();

                film.DateChange = System.currentTimeMillis();

                film.IsPrivate = !cbPrivate.isChecked();


                film.ImagePath = imagePath;
                film.ImageURL = imageUrl;

                if ( action == Constants.TYPE_SAW)
                {
                    if (cbTime.isChecked())
                    {
                        film.DateTime = HelpUtils.getTimeMills(System.currentTimeMillis());
                        film.IsSawToday = true;
                    } else
                    {
                        film.DateTime = dateSaw;
                        film.IsSawToday = false;
                    }

                    if ( dateSaw > 0 && item != null)
                    {
                        film.DateTime = dateSaw;
                    }
                }
                else if ( action == Constants.TYPE_WANT_TO_SEE)
                {
                    film.DateRemember = dateRemember;
                }



                if ( action == Constants.TYPE_SAW && film.Mark == 0)
                {
                    action = Constants.TYPE_DO_NOT_LIKE;
                }

				if ( item != null)
				{
				   film.Id = item.Id;
				}

                film.Comment = comment;

                film.Action = action;

                if ( film.Action == Constants.TYPE_SAW || film.Action == Constants.TYPE_DO_NOT_LIKE ) // проверить подключение к интернету
                {
                    if ( film.Action == Constants.TYPE_SAW )
                    {
                       String text =  comment + "\n" +  getActivity().getResources().getString(R.string.strMark) + film.Mark + " из 10";
                    }

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage(R.string.tell_friends);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.EXTRA_ITEM, film);

                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });

                    alert.setNeutralButton(android.R.string.no,new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.EXTRA_ITEM, film);

                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });

                    alert.show();



                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_ITEM, film);


                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }

                /* 1 объявить
                2 создать строковую переменную
                3 поставить слушатель событий
                4 сохранить отзыв
                5 добавить возможность репоста
                6 добавить в спинер видимость
                 */


				
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				getActivity().setResult(getActivity().RESULT_CANCELED);
				getActivity().finish();
				
			}
		});
		
		btnRemember = (Button) view.findViewById(R.id.filmBtnRemember);
		btnRemember.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String text = dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year;
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, monthOfYear);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                dateRemember = calendar.getTimeInMillis();
                                //  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

                            }
                        }, mYear, mMonth, mDay);
                dpd.setCancelable(false);
                dpd.show();

            }
        });
		


        cbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false && showCb == true) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    String text = dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    cbTime.setText(text);

                                    dateSaw = HelpUtils.getTimeMills( calendar.getTimeInMillis());
                                    //  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

                                }
                            }, mYear, mMonth, mDay);
                    dpd.setCancelable(false);
                    dpd.show();
                } else {
                    cbTime.setText(R.string.str_saw_today);
                }
                showCb = true;
            }
        });



        iv.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String value = editName.getText().toString();

                if (!TextUtils.isEmpty(value))
                {
                    Intent intent = new Intent(getActivity(), ImageDownloadActivity.class);
                    intent.putExtra(Constants.EXTRA_ITEM, value);
                    startActivityForResult(intent, Constants.CODE_ADD_IMAGE );
                }

            }
        });


        btnComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(R.string.thinking);
                final EditText edit = new EditText(getActivity());
                edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                if (item != null)
                {
                    edit.setText(item.Comment);
                }

                alert.setView(edit);
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        comment = edit.getText().toString();
                    }
                });
                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                alert.show();
            }
        });
		
		return view;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == Constants.CODE_ADD_IMAGE && resultCode == getActivity().RESULT_OK && data != null )
        {
            Bitmap bitmap = (Bitmap) data.getParcelableExtra(Constants.EXTRA_ITEM);
            imageUrl = data.getStringExtra(Constants.EXTRA_USER);

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_FILM) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
             imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_FILM,id,0);
                showImage(imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showImage(String path)
    {

        if ( !TextUtils.isEmpty(path) )
        {
            path = Constants.FILE + imagePath ;
            Picasso.with(getActivity())
                    .load(path)
                    .placeholder(R.drawable.icon_stop)
                    .error(R.drawable.icon_stop)
                    .into(target);

        }

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {
            iv.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable)
        {
            if ( AppContext.isNetworkAvailable(getActivity()))
            {
                Picasso.with(getActivity())
                        .load(imageUrl)
                        .placeholder(R.drawable.icon_stop)
                        .error(R.drawable.icon_stop)
                        .into(targetInternet);
            }
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }


    };

    private Target targetInternet = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {
            iv.setImageBitmap(bitmap);

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_FILM) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
                imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_FILM,id,0);
                showImage(imagePath);
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable)
        {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }


    };





}
