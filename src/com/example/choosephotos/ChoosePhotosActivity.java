package com.example.choosephotos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ChoosePhotosActivity extends Activity {
	List<ImageBucket> dataList;
	List<ImageItem> imageLists;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_image_bucket);
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		imageLists = new ArrayList<ImageItem>();
		dataList = helper.getImagesBucketList(false,imageLists);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(ChoosePhotosActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ChoosePhotosActivity.this,
						ImageGridActivity.class);
//				intent.putExtra(ChoosePhotosActivity.EXTRA_IMAGE_LIST,
//						(Serializable) dataList.get(position).imageList);
				Log.i("imageLists", imageLists.size() + "");
				intent.putExtra(ChoosePhotosActivity.EXTRA_IMAGE_LIST,
						(Serializable) imageLists);
				startActivityForResult(intent, 100);
			}

		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=Activity.RESULT_OK){
			return;
		}
		
		switch (requestCode) {
		
		case 100:
			setResult(Activity.RESULT_OK);
			finish();
			break;

		default:
			break;
		}
	}
}
