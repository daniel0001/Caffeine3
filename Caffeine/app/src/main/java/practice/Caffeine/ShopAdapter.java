package practice.Caffeine;

/**
 * Created by Daniel on 06/03/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;


public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private Context mContext;
    private List<ShopCard> shopList;

    public ShopAdapter(Context mContext, List<ShopCard> shopList) {
        this.mContext = mContext;
        this.shopList = shopList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ShopCard shopCard = shopList.get(position);
        holder.title.setText(shopCard.getName());
        final Boolean addNewShop = (shopCard.getName() == mContext.getString(R.string.add_new_coffee_shop));

        if (addNewShop) {
            holder.visits.setText("");

        } else {
            holder.visits.setText(shopCard.getNumOfVisits() + " out of 9 Points Collected");
        }

        // loading shop card using Glide library
        Glide.with(mContext).load(shopCard.getShopImage()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper myDB = new DatabaseHelper(mContext);
                User user = myDB.getUser(1);
                int userID = user.getUserID();
                String name = user.getName();

                if (addNewShop) {
                    Intent intent = new Intent(mContext, NewCoffeeShopActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("name", name);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, VisitDetailsActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("userID", userID);
                    intent.putExtra("shopName", shopCard.getName());
                    intent.putExtra("shopAddress", shopCard.getShopAddress());
                    intent.putExtra("shopPhone", shopCard.getShopPhone());
                    intent.putExtra("lat", shopCard.getLat());
                    intent.putExtra("lng", shopCard.getLng());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_shop, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, visits;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            visits = (TextView) view.findViewById(R.id.visits);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }

    /**
     * Click listener for popup menu items
     */
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private MyMenuItemClickListener() {
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_shop_details:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_add_visit:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}