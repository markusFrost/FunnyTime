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
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.BitmapUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.HelpUtils;

public class CUSerialFragment extends Fragment 
{
	EditText editName, editSeason, editSeries;
	RatingBar rb;
    ImageView iv;
	
	Spinner spinner;
	Button btnSave, btnCancel, btnRemember, btnComment;
	
	Serial item;
	int action = -1;
    CheckBox cbPrivate;
    CheckBox cbTime;

    boolean showCb = true;
    long dateRemember = 0;
    long dateSaw = 0;

    String imagePath = "";
    String imageUrl = "";
    String comment = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.cu_serial_layout, container, false);
		
		spinner = (Spinner) view.findViewById(R.id.serialSpinner);
		editName = (EditText) view.findViewById(R.id.serialEditName);
		editSeason = (EditText) view.findViewById(R.id.serialEditSeason);
		editSeries = (EditText) view.findViewById(R.id.serialEditSeries);
		editSeason.setText("");
		editSeries.setText("");
		
		rb = (RatingBar) view.findViewById(R.id.serialRatingBar);

        iv = (ImageView) view.findViewById(R.id.serial_iv);

        cbPrivate = (CheckBox) view.findViewById(R.id.serialCbShowAll);
        cbTime = (CheckBox) view.findViewById(R.id.serialCbToday);
        cbPrivate.setChecked(true);
        cbTime.setChecked(true);
		
		
		btnSave = (Button) view.findViewById(R.id.serialBtnOk);
		btnCancel = (Button) view.findViewById(R.id.serialBtnCancel);
        btnComment = (Button) view.findViewById(R.id.serialBtnComment);
		
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
				getActivity().getResources().getStringArray(R.array.strArraySerial));
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
					btnRemember.setVisibility(View.VISIBLE);
					editSeason.setVisibility(View.GONE);
					editSeries.setVisibility(View.GONE);
                    cbTime.setVisibility(View.GONE);
                    btnComment.setVisibility(View.GONE);
					
				}
				else if (position == 1)
				{
                    action = Constants.TYPE_ALREADY_SEE;
					rb.setVisibility(View.GONE);
					btnRemember.setVisibility(View.GONE);
					editSeason.setVisibility(View.VISIBLE);
					editSeries.setVisibility(View.VISIBLE);
                    cbTime.setVisibility(View.GONE);
                    btnComment.setVisibility(View.GONE);
				}
				else if (position == 2)
				{
                    action = Constants.TYPE_SAW;
					rb.setVisibility(View.VISIBLE);
					btnRemember.setVisibility(View.GONE);
					editSeason.setVisibility(View.GONE);
					editSeries.setVisibility(View.GONE);
                    cbTime.setVisibility(View.VISIBLE);
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
			item = (Serial) getArguments().getSerializable(Constants.EXTRA_ITEM);
	   	       //   Toast.makeText(getActivity(), item.Id + "", Toast.LENGTH_SHORT).show();
		}
		else
		{

		}
		
		
		if ( item != null)
		{
			editName.setText(item.Name);
			if (item.Season != 0 && item.Series != 0)
			{
				editSeason.setText(item.Season + "");
				editSeries.setText(item.Series + "");
			}

            cbPrivate.setChecked(!item.IsPrivate);

            imagePath = item.ImagePath;
            imageUrl = item.ImageURL;
            showImage(item.ImagePath);
			
			rb.setRating(item.Mark);
			action = item.Action;
			if ( action ==  Constants.TYPE_WANT_TO_SEE)
			{
                dateRemember = item.DateRemember;
				spinner.setSelection(0);
			}
			else if ( action ==  Constants.TYPE_ALREADY_SEE)
			{
				spinner.setSelection(1);
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
				spinner.setSelection(2);
			}
		}
		else
		{
			rb.setRating(0);
			spinner.setSelection(0);
		}
		
		btnSave.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				final Serial serial = new Serial();
				serial.Name = editName.getText().toString();
				serial.Mark = rb.getRating();
                serial.IsPrivate = !cbPrivate.isChecked();
                serial.DateChange = System.currentTimeMillis();
                serial.ImageURL = imageUrl;
                serial.ImagePath = imagePath;

				if (item != null)
				{
					serial.Id = item.Id;
				}


                if ( action == Constants.TYPE_WANT_TO_SEE)
                {
                    serial.DateRemember = dateRemember;
                }
				else if (action ==  Constants.TYPE_ALREADY_SEE)
				{
					try
					{
					serial.Series = Integer.parseInt(editSeries.getText().toString());
					serial.Season  = Integer.parseInt(editSeason.getText().toString());
					} catch (Exception e){}
				}
                else if ( action == Constants.TYPE_SAW)
                {
                    if ( cbTime.isChecked())
                    {
                        serial.DateTime = HelpUtils.getTimeMillsDay(System.currentTimeMillis());
                        serial.IsSawToday = true;
                    }
                    else
                    {
                        serial.DateTime = dateSaw;
                        serial.IsSawToday = false;
                    }
                    if ( serial.Mark == 0)
                    {
                        action = Constants.TYPE_DO_NOT_LIKE;
                    }

                    if ( dateSaw > 0 && item != null)
                    {
                        serial.DateTime = dateSaw;
                    }
                }

                serial.Action = action;
                serial.Comment = comment;

                if ( serial.Action == Constants.TYPE_SAW || serial.Action == Constants.TYPE_DO_NOT_LIKE ) // проверить подключение к интернету
                {
                    if ( serial.Action == Constants.TYPE_SAW )
                    {
                        String text =  comment + "\n" +  getActivity().getResources().getString(R.string.strMark) + serial.Mark + " из 10";
                    }

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage(R.string.tell_friends);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.EXTRA_ITEM, serial);


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
                            intent.putExtra(Constants.EXTRA_ITEM, serial);


                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });

                    alert.show();



                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_ITEM, serial);


                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }
				
				/*Intent intent = new Intent();
				intent.putExtra(Constants.EXTRA_ITEM, serial);
				
				
				getActivity().setResult(getActivity().RESULT_OK, intent);
				getActivity().finish();*/
				
				
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
		
		
		btnRemember = (Button) view.findViewById(R.id.serialBtnRemember);
		btnRemember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				final Calendar c = Calendar.getInstance();
				int 	mYear = c.get(Calendar.YEAR);
				int 	mMonth = c.get(Calendar.MONTH);
				int	mDay = c.get(Calendar.DAY_OF_MONTH);
					 
					DatePickerDialog dpd = new DatePickerDialog(getActivity(),
					        new DatePickerDialog.OnDateSetListener() {
					 
					            @Override
					            public void onDateSet(DatePicker view, int year,
					                    int monthOfYear, int dayOfMonth) {
					               String text =  dayOfMonth + "-"
					                        + (monthOfYear + 1) + "-" + year;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    dateRemember = calendar.getTimeInMillis();
					              // Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
					 
					            }
					        }, mYear, mMonth, mDay);
					dpd.show();
				
			}
		});


        cbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked == false && showCb ==  true)
                {
                    final Calendar c = Calendar.getInstance();
                    int 	mYear = c.get(Calendar.YEAR);
                    int 	mMonth = c.get(Calendar.MONTH);
                    int	mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth)
                                {
                                    String text =  dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year;
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    dateSaw = HelpUtils.getTimeMills( calendar.getTimeInMillis());
                                    cbTime.setText(HelpUtils.getTimeString(dateSaw));
                                    //  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

                                }
                            }, mYear, mMonth, mDay);
                    dpd.setCancelable(false);
                    dpd.show();
                }
                else
                {
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

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_SERIAL) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
                imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_SERIAL, id, 0);
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

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_SERIAL) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
                imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_SERIAL,id,0);
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
