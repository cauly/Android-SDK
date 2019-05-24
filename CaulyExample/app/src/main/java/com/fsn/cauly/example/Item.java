package com.fsn.cauly.example;


public class Item
{
	 public int img, icon;
	 public String  title, subTitle, description, tag;
	 Item(int img, int icon, String title, String subTitle,String description,String tag)
	 {
		 this.icon = icon;
		 this.img = img;
		 this.title =title;
		 this.subTitle = subTitle;
		 this.tag = tag;
		 this.description = description;
	 }
	 Item( int icon, String title, String subTitle,String description,String tag)
	 {
		 this.icon = icon;
		 this.title =title;
		 this.subTitle = subTitle;
		 this.tag = tag;
		 this.description = description;
	 }
}