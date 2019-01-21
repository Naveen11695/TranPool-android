

package naveen.hackathon.hackathon.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import naveen.hackathon.hackathon.domain.mock.database_handler;
import naveen.hackathon.hackathon.model.CenterRepository;
import naveen.hackathon.hackathon.activities.ECartHomeActivity;
import naveen.hackathon.hackathon.adapter.ProductsInCategoryPagerAdapter;
import naveen.hackathon.hackathon.fragment.ProductListFragment;

import java.util.Set;

/**
 * The Class ImageLoaderTask.
 */
public class ProductLoaderTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabs;
    private RecyclerView recyclerView;

    public ProductLoaderTask(RecyclerView listView, Context context,
                             ViewPager viewpager, TabLayout tabs) {

        this.viewPager = viewpager;
        this.tabs = tabs;
        this.context = context;

    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        setUpUi();

    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        database_handler.getFakeWebServer().getAllRawmaterial();

        return null;
    }

    private void setUpUi() {

        setupViewPager();
    }

    private void setupViewPager() {

        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                ((ECartHomeActivity) context).getSupportFragmentManager());

        Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                .keySet();

        for (String string : keys) {

            adapter.addFrag(new ProductListFragment(string), string);

        }

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

    }

}