package net.odiak.interpolators;

import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final Class[] interpolators = new Class[] {
            AccelerateDecelerateInterpolator.class,
            AccelerateInterpolator.class,
            AnticipateInterpolator.class,
            AnticipateOvershootInterpolator.class,
            BounceInterpolator.class,
            DecelerateInterpolator.class,
            FastOutLinearInInterpolator.class,
            FastOutSlowInInterpolator.class,
            LinearInterpolator.class,
            LinearOutSlowInInterpolator.class,
            OvershootInterpolator.class,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);

        InterpolatorsAdapter adapter = new InterpolatorsAdapter(this);
        adapter.addAll(interpolators);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= interpolators.length) return;

                animateWithInterpolator(view, interpolators[position]);
            }
        });
    }

    private void animateWithInterpolator(View view, Class interpolatorClass) {
        InterpolatorViewHolder holder = (InterpolatorViewHolder) view.getTag();
        final TextView textView = holder.textView;

        Interpolator interpolator;
        try {
            interpolator = (Interpolator) interpolatorClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            return;
        }

        Animation shrink = new ScaleAnimation(1f, 0.2f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        shrink.setDuration(1200);
        shrink.setInterpolator(interpolator);

        final Animation grow = new ScaleAnimation(0.2f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);
        grow.setDuration(1200);
        grow.setInterpolator(interpolator);

        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.startAnimation(grow);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        textView.startAnimation(shrink);
    }

    private static class InterpolatorsAdapter extends ArrayAdapter<Class> {

        private LayoutInflater mLayoutInflater;

        public InterpolatorsAdapter(Context context) {
            super(context, 0);
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InterpolatorViewHolder holder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.main_list_item, parent, false);
                holder = new InterpolatorViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (InterpolatorViewHolder) convertView.getTag();
            }

            Class interpolatorClass = getItem(position);
            holder.textView.setText(interpolatorClass.getSimpleName());

            return convertView;
        }
    }

    private static class InterpolatorViewHolder {
        TextView textView;
    }
}