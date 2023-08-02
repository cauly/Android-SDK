package com.fsn.cauly.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JavaEntryActivity extends Activity   {

	ListView listview; 
	static final String[] type = {"동적 배너및 전면","XML배너 및 전면", "네이티브 리스트뷰 타입", "네이티브 뷰타입","네이티브 카드뷰 타입","네이티브 데이터 타입","종료팝업"};
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_entry);
		listview = (ListView) findViewById(R.id.native_area);
		ArrayAdapter<String>   myAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, type);  
		listview.setAdapter(myAdapter );
		listview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				if(position==0)
					startActivity(new Intent(JavaEntryActivity.this, JavaActivity.class));
				else if(position==1)
					startActivity(new Intent(JavaEntryActivity.this, JavaXMLActivity.class));
				else if(position==2)
					startActivity(new Intent(JavaEntryActivity.this, JavaNativeListActivity.class));
				else if(position==3)
					startActivity(new Intent(JavaEntryActivity.this, JavaNativeViewActivity.class));
				else if(position==4)
					startActivity(new Intent(JavaEntryActivity.this, JavaNativeCardActivity.class));
				else if(position==5)
					startActivity(new Intent(JavaEntryActivity.this, JavaNativeDataActivity.class));
				else 
					startActivity(new Intent(JavaEntryActivity.this, JavaCloseActivity.class));
			}
		});
    }
	
}
