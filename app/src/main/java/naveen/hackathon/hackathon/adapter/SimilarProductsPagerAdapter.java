
package naveen.hackathon.hackathon.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import naveen.hackathon.hackathon.R;
import naveen.hackathon.hackathon.model.CenterRepository;
import naveen.hackathon.hackathon.model.entities.Money;
import naveen.hackathon.hackathon.util.ColorGenerator;
import naveen.hackathon.hackathon.customview.TextDrawable;
import naveen.hackathon.hackathon.customview.TextDrawable.IBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class SimilarProductsPagerAdapter extends PagerAdapter {

    /**
     * The m context.
     */
    Context mContext;

    /**
     * The m layout inflater.
     */
    LayoutInflater mLayoutInflater;

    private String productCategory;

    private ImageView imageView;

    private IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    /**
     * Instantiates a new home slides pager adapter.
     *
     * @param context the context
     */
    public SimilarProductsPagerAdapter(Context context, String productCategory) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productCategory = productCategory;
    }

    @Override
    public int getCount() {

        if (null != CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                && null != CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(productCategory)) {
            return CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                    .get(productCategory).size();
        }

        return 0;
    }

     @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_category_list, container,
                false);

        imageView = (ImageView) itemView.findViewById(R.id.imageView);

        mDrawableBuilder = TextDrawable.builder().beginConfig().withBorder(4)
                .endConfig().roundRect(10);

        drawable = mDrawableBuilder.build(
                String.valueOf(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(productCategory).get(position)
                        .getItemName().charAt(0)),

                mColorGenerator.getColor(CenterRepository.getCenterRepository()
                        .getMapOfProductsInCategory().get(productCategory).get(position)
                        .getItemName()));

        final String ImageUrl = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .get(productCategory).get(position).getImageURL();

        Picasso.get().load(ImageUrl).placeholder(drawable)
                .error(drawable).fit().centerCrop()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        // Try again online if cache failed

                        Picasso.get().load(ImageUrl)
                                .placeholder(drawable).error(drawable).fit()
                                .centerCrop().into(imageView);
                    }
                });

        ((TextView) itemView.findViewById(R.id.item_name))
                .setText(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                        .get(productCategory).get(position).getItemName());

        ((TextView) itemView.findViewById(R.id.item_short_desc1))
                .setText(CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                        .get(productCategory).get(position).getItemDetail());

        ((TextView) itemView.findViewById(R.id.category_discount))
                .setText(Money.rupees(
                        BigDecimal.valueOf(Long.valueOf(CenterRepository
                                .getCenterRepository().getMapOfProductsInCategory()
                                .get(productCategory).get(position)
                                .getSellMRP()))).toString());

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }


    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getPageWidth(int position) {
        return (0.5f);
    }


    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub

    }
}