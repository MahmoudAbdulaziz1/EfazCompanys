package com.taj51.efazcompany.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.taj51.efazcompany.OfferComponentActivity;
import com.taj51.efazcompany.R;
import com.taj51.efazcompany.api_classes.Api;
import com.taj51.efazcompany.pojo.GetProfilePojo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomAdapter extends RecyclerView.Adapter {


    private Context context;

    private ArrayList<Integer> companyId;
    private ArrayList<String> productTitlesArr;
    private ArrayList<String> productImagesArr;
    private ArrayList<String> daysArr;
    private ArrayList<String> hoursArr;
    private ArrayList<String> minutesArr;
    private ArrayList<Integer> productIds;
    private ArrayList<Double> productCosts;
    private ArrayList<String> productExplanation;
    //private int companyId;

    public CustomAdapter(Context context, ArrayList<Integer> companyId, ArrayList<String> productTitlesArr, ArrayList<String> productImagesArr,
                         ArrayList<String> daysArr, ArrayList<String> hoursArr, ArrayList<String> minutesArr, ArrayList<Integer> productIds, ArrayList<Double> productCosts,
                         ArrayList<String> productExplanation) {

        this.context = context;
//        this.companyLogoStr = companyLogoStr;
//        this.companyNameStr = companyNameStr;
        this.companyId = companyId;
        this.productTitlesArr = productTitlesArr;
        this.productImagesArr = productImagesArr;
        this.daysArr = daysArr;
        this.hoursArr = hoursArr;
        this.minutesArr = minutesArr;
        this.productIds = productIds;
        this.productCosts = productCosts;
        this.productExplanation = productExplanation;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.screen_offer_plus, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.d("adapters", companyId+" s");

        Api.getClient().getProfile(companyId.get(position)).enqueue(new Callback<GetProfilePojo>() {
            @Override
            public void onResponse(Call<GetProfilePojo> call, Response<GetProfilePojo> response) {
                Log.d("adapters", "response 1");
                String companyLogo = response.body().getCompany_logo_image();
                Log.d("adapters", companyLogo);
                String companyName = response.body().getCompany_name();
                Log.d("adapters", companyName);
                ((MyViewHolder) holder).companyName.setText(companyName);
                if (companyLogo.equals(null)) {
                    ((MyViewHolder) holder).companyLogo.setImageResource(R.drawable.efaz_logo);
                } else {
                    ((MyViewHolder) holder).companyLogo.setImageBitmap(decodeBase64(companyLogo));
                }


            }

            @Override
            public void onFailure(Call<GetProfilePojo> call, Throwable t) {
                Log.d("adapters", t.getMessage());
            }
        });



        ((MyViewHolder) holder).productTitle.setText(productTitlesArr.get(position));
        if (productImagesArr.get(position).equals(null)) {
            ((MyViewHolder) holder).companyLogo.setImageResource(R.drawable.backgrd);
        } else {
            ((MyViewHolder) holder).productImage.setImageBitmap(decodeBase64(productImagesArr.get(position)));
        }
        if (Integer.parseInt(daysArr.get(position)) <= 0 && Integer.parseInt(hoursArr.get(position)) <= 0 && Integer.parseInt(minutesArr.get(position)) <= 0) {
            ((MyViewHolder) holder).days.setText("0");
            ((MyViewHolder) holder).hours.setText("0");
            ((MyViewHolder) holder).minutes.setText("0");
            ((MyViewHolder) holder).availableTxt.setText("Closed");
            ((MyViewHolder) holder).availableLinear.setCardBackgroundColor(Color.parseColor("#FFFF6363"));
        } else {
            ((MyViewHolder) holder).days.setText(daysArr.get(position));
            ((MyViewHolder) holder).hours.setText(hoursArr.get(position));
            ((MyViewHolder) holder).minutes.setText(minutesArr.get(position));
            ((MyViewHolder) holder).availableTxt.setText("Available");
            ((MyViewHolder) holder).availableLinear.setCardBackgroundColor(Color.parseColor("#FF74F28E"));
        }
//        ((MyViewHolder)holder).days.setText(daysArr.get(position));
//        ((MyViewHolder)holder).hours.setText(hoursArr.get(position));
//        ((MyViewHolder)holder).minutes.setText(minutesArr.get(position));
        ((MyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OfferComponentActivity.class);
                intent.putExtra("product_id", productIds.get(position));
                intent.putExtra("product_cost", productCosts.get(position));
                intent.putExtra("product_explain", productExplanation.get(position));
                intent.putExtra("product_image", productImagesArr.get(position));
                intent.putExtra("product_title", productTitlesArr.get(position));
                intent.putExtra("display", daysArr);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productTitlesArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's

        ImageView companyLogo;
        TextView companyName;
        TextView productTitle;
        ImageView productImage;
        EditText days;
        EditText hours;
        EditText minutes;
        TextView availableTxt;
        CardView availableLinear;

        public MyViewHolder(View itemView) {
            super(itemView);

            companyLogo = (ImageView) itemView.findViewById(R.id.offer_screen_company_logo);
            companyName = (TextView) itemView.findViewById(R.id.offer_screen_company_name);
            productTitle = (TextView) itemView.findViewById(R.id.offer_screen_title);
            productImage = (ImageView) itemView.findViewById(R.id.screen_offer_product_image);
            days = (EditText) itemView.findViewById(R.id.remain_days);
            hours = (EditText) itemView.findViewById(R.id.remain_hours);
            minutes = (EditText) itemView.findViewById(R.id.remain_minutes);
            availableLinear = (CardView) itemView.findViewById(R.id.available);
            availableTxt = (TextView) itemView.findViewById(R.id.offer_screen_available);

        }
    }
}
