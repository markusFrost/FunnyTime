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
import android.widget.AutoCompleteTextView;
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
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.BitmapUtils;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.HelpUtils;


public class CUBookFragment extends Fragment 
{
	EditText editName,editPage;
	AutoCompleteTextView editAuthor;
	RatingBar rb;
	Spinner spinner;
    ImageView iv;
	
	Button btnSave, btnCancel, btnRemember, btnComment;

	Book item = null;

    int action = -1;
    CheckBox cbPrivate;
    CheckBox cbTime;

    boolean showCb = true;
    long dateRemember = 0;
    long dateSaw = 0;

    ArrayAdapter<String> adapter;
    String imagePath = "";
    String imageUrl = "";
    String comment = "";
	
   @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState)
   {
	   View view = inflater.inflate(R.layout.cu_book_layout, container, false);
	   
	   editName = (EditText) view.findViewById(R.id.bookEditName);
	   editAuthor = (AutoCompleteTextView) view.findViewById(R.id.bookEditAuthor);
	   editPage = (EditText) view.findViewById(R.id.bookEditPage);
	   editPage.setText("");

	   spinner = (Spinner) view.findViewById(R.id.bookSpinner);

       cbPrivate = (CheckBox) view.findViewById(R.id.bookCbShowAll);
       cbTime = (CheckBox) view.findViewById(R.id.bookCbToday);
       cbPrivate.setChecked(true);
       cbTime.setChecked(true);

       iv = (ImageView) view.findViewById(R.id.book_iv);
	   
	   rb = (RatingBar) view.findViewById(R.id.bookRatingBar);
	   
 adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1,
			  AppContext.dbAdapter.getAuthors(0)
			  );
	  
	  editAuthor.setAdapter(adapter);
	  
	   
	   btnSave = (Button) view.findViewById(R.id.bookBtnOk);
		btnCancel = (Button) view.findViewById(R.id.bookBtnCancel);
       btnComment = (Button) view.findViewById(R.id.bookBtnComment);
		
		ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,
				getActivity().getResources().getStringArray(R.array.strArrayBook));
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
					editPage.setVisibility(View.GONE);
					rb.setVisibility(View.GONE);
					btnRemember.setVisibility(View.VISIBLE);
                    cbTime.setVisibility(View.GONE);
                    btnComment.setVisibility(View.GONE);
				}
				else if ( position == 1)
				{
                    action = Constants.TYPE_ALREADY_SEE;
					editPage.setVisibility(View.VISIBLE);
					rb.setVisibility(View.GONE);
					btnRemember.setVisibility(View.GONE);
                    cbTime.setVisibility(View.GONE);
                    btnComment.setVisibility(View.GONE);
				}
				else
				{
                    action = Constants.TYPE_SAW;
					editPage.setVisibility(View.GONE);
					rb.setVisibility(View.VISIBLE);
					btnRemember.setVisibility(View.GONE);
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
            item = (Book) getArguments().getSerializable(Constants.EXTRA_ITEM);
		}
		else
		{

		}
		
		if (item != null)
		{
			editName.setText(item.Name);
			editAuthor.setText(item.Author);
			editPage.setText(item.Page + "");

            imagePath = item.ImagePath;
            imageUrl = item.ImageURL;
            showImage(item.ImagePath);

			rb.setRating(item.Mark);
			action = item.Action;
            cbPrivate.setChecked(!item.IsPrivate);
            if ( action ==  Constants.TYPE_WANT_TO_SEE)
			{
				spinner.setSelection(0);
			}
            else if ( action ==  Constants.TYPE_ALREADY_SEE)
			{
                dateRemember = item.DateRemember;
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
			spinner.setSelection(0);
			rb.setRating(0);
		}
		
		btnSave.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				final Book book = new Book();
				book.Name = editName.getText().toString();
				book.Mark = rb.getRating();
                book.IsPrivate = !cbPrivate.isChecked();
                book.DateChange = System.currentTimeMillis();
				book.Author = editAuthor.getText().toString();

                book.ImageURL = imageUrl;
                book.ImagePath = imagePath;

				if (item != null)
				{
					book.Id = item.Id;
					book.Page = item.Page;
					
				}

                if ( action == Constants.TYPE_WANT_TO_SEE)
                {
                    book.DateRemember = dateRemember;
                }
                else if (action ==  Constants.TYPE_ALREADY_SEE)
                {
                    try
                    {
                        book.Page = Integer.parseInt(editPage.getText().toString());
                    }catch (Exception e){}
                }
                else if ( action == Constants.TYPE_SAW)
                {

                    if ( cbTime.isChecked())
                    {
                        book.DateTime = HelpUtils.getTimeMillsDay(System.currentTimeMillis());
                        book.IsSawToday = true;
                    }
                    else
                    {
                        book.DateTime = dateSaw;
                       book.IsSawToday = false;
                    }
                    if ( book.Mark == 0)
                    {
                        action = Constants.TYPE_DO_NOT_LIKE;
                    }

                    if ( dateSaw > 0 && item != null)
                    {
                        book.DateTime = dateSaw;
                    }
                }

                book.Action = action;
                book.Comment = comment;


                if ( book.Action == Constants.TYPE_SAW || book.Action == Constants.TYPE_DO_NOT_LIKE ) // проверить подключение к интернету
                {
                    if ( book.Action == Constants.TYPE_SAW )
                    {
                        String text =  comment + "\n" +  getActivity().getResources().getString(R.string.strMark) + book.Mark + " из 10";
                    }

                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setMessage(R.string.tell_friends);
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent();
                            intent.putExtra(Constants.EXTRA_ITEM, book);


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
                            intent.putExtra(Constants.EXTRA_ITEM, book);


                            getActivity().setResult(getActivity().RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });

                    alert.show();



                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_ITEM, book);


                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }

				
				/*Intent intent = new Intent();
				intent.putExtra(Constants.EXTRA_ITEM, book);
				
				
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
		
		btnRemember = (Button) view.findViewById(R.id.bookBtnRemember);
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
					             //  Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, year);
                                    calendar.set(Calendar.MONTH, monthOfYear);
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    dateRemember = calendar.getTimeInMillis();
					 
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
               String value = editAuthor.getText().toString() + " " +  editName.getText().toString();

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

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_BOOK) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
                imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_BOOK, id, 0);
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

            long id = AppContext.dbAdapter.getLastId(Constants.TYPE_BOOK) + 1;

            if ( item != null)
            {
                id = item.Id;
            }

            try
            {
                imagePath =    BitmapUtils.saveAsImage(bitmap, Constants.TYPE_BOOK,id,0);
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
