package com.example.choosephotos;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.choosephotos.BitmapCache.ImageCallback;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ImageGridAdapter extends BaseAdapter {

	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	Activity act;
	List<ImageItem> dataList;
	Map<String, String> map = new HashMap<String, String>();
	BitmapCache cache;
	private Handler mHandler;
	private int selectTotal = 0;
	ImageCallback callback = new ImageCallback() {
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public ImageGridAdapter(Activity act, List<ImageItem> list, Handler mHandler) {
		this.act = act;
		dataList = list;
		cache = new BitmapCache();
		this.mHandler = mHandler;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
		//private RelativeLayout rl_takephoto;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(act, R.layout.item_image_grid, null);
			//holder.rl_takephoto = (RelativeLayout) convertView.findViewById(R.id.rl_takephoto);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);
		
//		if(position == 0){
//			holder.rl_takephoto.setVisibility(View.VISIBLE);
//		}else{
//			holder.rl_takephoto.setVisibility(View.GONE);
			holder.iv.setTag(item.imagePath);
			cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
					callback);
			if (item.isSelected) {
				holder.selected.setImageResource(R.drawable.icon_data_select);  
				holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
			} else {
				holder.selected.setImageResource(-1);
				holder.text.setBackgroundColor(0x00000000);
			}
//		}
//		holder.rl_takephoto.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Message message = Message.obtain(mHandler, 1);
//				message.sendToTarget();
//			}
//		});
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(position == 0){
					Message message = Message.obtain(mHandler, 1);
					message.sendToTarget();
				} else{
					String path = dataList.get(position).imagePath;
					if ((Bimp.bmp.size() + selectTotal) < 9) {
						item.isSelected = !item.isSelected;
						if (item.isSelected) {
							int index = path.lastIndexOf("/");
//							String desPath = path.substring(0, index + 1) + System.currentTimeMillis() + ".png";
							String desPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/systemmobile/"
									+ System.currentTimeMillis() + ".png";
							System.out.println("desPath:" + desPath);
							Bitmap bitmap = ImageFactory.ratio(path, 1280f, 960f);
							try {
								ImageFactory.storeImage(bitmap, desPath);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							//PhotosUtil.compressPicture(path, desPath);
							holder.selected
									.setImageResource(R.drawable.icon_data_select);
							holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
							selectTotal++;
							if (textcallback != null)
								textcallback.onListen(selectTotal);
							map.put(path, path);

						} else if (!item.isSelected) {
							holder.selected.setImageResource(-1);
							holder.text.setBackgroundColor(0x00000000);
							selectTotal--;
							if (textcallback != null)
								textcallback.onListen(selectTotal);
							map.remove(path);
						}	
					} else if ((Bimp.bmp.size() + selectTotal) >= 9) {
						if (item.isSelected == true) {
							item.isSelected = !item.isSelected;
							holder.selected.setImageResource(-1);
							selectTotal--;
							map.remove(path);

						} else {
							Message message = Message.obtain(mHandler, 0);
							message.sendToTarget();
						}
					}
				}
			}

		});

		return convertView;
	}
}
