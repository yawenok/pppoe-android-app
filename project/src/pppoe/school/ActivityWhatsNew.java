package pppoe.school;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;

public class ActivityWhatsNew extends Activity {

	private ImageView viewImage;
	private ImageView[] viewsImage;

	private ArrayList<View> arrayPages;
	private ViewPager viewPager;

	private ViewGroup viewgPictures;
	private ViewGroup viewgPoints;

	private PopDialog popMenu;

	private AppConfig appConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		popMenu = new PopDialog(ActivityWhatsNew.this);
		appConfig = new AppConfig(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		final LayoutInflater inflater = getLayoutInflater();

		arrayPages = new ArrayList<View>();
		arrayPages.add(inflater.inflate(R.layout.viewpager01, null));
		arrayPages.add(inflater.inflate(R.layout.viewpager02, null));
		arrayPages.add(inflater.inflate(R.layout.viewpager03, null));
		arrayPages.add(inflater.inflate(R.layout.viewpager04, null));

		viewsImage = new ImageView[arrayPages.size()];
		viewgPictures = (ViewGroup) inflater.inflate(R.layout.viewpagers, null);

		viewPager = (ViewPager) viewgPictures.findViewById(R.id.guidePagers);
		viewgPoints = (ViewGroup) viewgPictures.findViewById(R.id.viewPoints);

		for (int i = 0; i < arrayPages.size(); i++) {
			viewImage = new ImageView(ActivityWhatsNew.this);
			viewImage.setLayoutParams(new LayoutParams(20, 20));
			viewImage.setPadding(5, 0, 5, 0);

			viewsImage[i] = viewImage;

			if (i == 0)
				viewsImage[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
			else
				viewsImage[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));

			viewgPoints.addView(viewsImage[i]);
		}

		setContentView(viewgPictures);

		viewPager.setAdapter(new NavigationPageAdapter());
		viewPager.setOnPageChangeListener(new NavigationPageChangeListener());

	}

	public void clickStart(View v) {
		appConfig.setNewVersion();

		Intent intent = new Intent(ActivityWhatsNew.this, ActivityMain.class);
		startActivity(intent);
		ActivityWhatsNew.this.finish();
	}

	public void typeClick(View v) {
		popMenu.show();
	}

	class NavigationPageAdapter extends PagerAdapter {
		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(arrayPages.get(position));
		}

		@Override
		public int getCount() {
			return arrayPages.size();
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(arrayPages.get(position));
			return arrayPages.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	class NavigationPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < viewsImage.length; i++) {
				viewsImage[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				if (position != i)
					viewsImage[i].setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
			}
		}
	}
}
