/* ListAdapter : 효율 좋은 리사이클러 뷰 */
/* item_basket.xml의 RV 연결 */
package com.example.nsu_alarmy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nsu_alarmy.data.BasketMenu;
import com.example.nsu_alarmy.data.BasketViewModel;
import com.example.nsu_alarmy.data.Option;

import java.text.DecimalFormat;
import java.util.List;

public class BasketStoreAdapter extends ListAdapter<BasketMenu, BasketStoreAdapter.BasketStoreViewHolder> {

    private Context context;
    private BasketViewModel basketViewModel;
    private String userId;

    public static int totalPriceAll = 0;


    // 어댑터 초기화
    public BasketStoreAdapter(String userId, Context context, BasketViewModel basketViewModel) {
        super(new BasketMenuDiffCallback()); // DiffUtil 연결 _바뀐 항목 갱신
        this.userId = userId;
        this.context = context;
        this.basketViewModel = basketViewModel;
    }

    /* RecyclerView 형태 설정 */
    public class BasketStoreViewHolder extends RecyclerView.ViewHolder {
        protected TextView menu, menuPrice, totalPrice, amount, optionName, optionPrice;
        protected ImageView deleteButton, plusButton, minusButton;
        protected LinearLayout optionContainer;


        public BasketStoreViewHolder(View view) {
            super(view);
            menu = view.findViewById(R.id.menu_name_textview);
            menuPrice = view.findViewById(R.id.menu_price_textview);
            totalPrice = view.findViewById(R.id.total_price_textview);
            amount = view.findViewById(R.id.amount_textview);
            optionName = view.findViewById(R.id.option_name_textview);
            optionPrice = view.findViewById(R.id.option_price_textview);
            optionContainer = view.findViewById(R.id.option_container);

            deleteButton = view.findViewById(R.id.btn_delete);
            plusButton = view.findViewById(R.id.btn_plus);
            minusButton = view.findViewById(R.id.btn_minus);
        }
    }

    @NonNull
    @Override // 뷰 생성
    public BasketStoreViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_basket_menu_each_store, viewGroup, false);
        return new BasketStoreViewHolder(view);
    }

    @Override // 뷰에 데이터 넣기
    public void onBindViewHolder(BasketStoreViewHolder basketStoreViewHolder, int position) {
        BasketMenu item = getItem(position);
        List<Option> options = item.getOptionList();
        DecimalFormat formatter = new DecimalFormat("#,###");

        // 메뉴 이름
        basketStoreViewHolder.menu.setText(item.getMenu());

        // 수량
        basketStoreViewHolder.amount.setText(String.valueOf(item.getAmount()) + "개");

        // 총 금액
        int itemTotalPrice = item.getMenuPrice();

        if (options != null) {
            for (Option option : options) {
                itemTotalPrice += option.getOptionPrice();
            }
        }
        itemTotalPrice *= item.getAmount();
        totalPriceAll += itemTotalPrice;

        basketStoreViewHolder.totalPrice.setText(formatter.format(itemTotalPrice) + "원");
        // 메뉴 가격
        basketStoreViewHolder.menuPrice.setText(formatter.format(item.getMenuPrice()) + "원");

        // 하단에 옵션 정보 추가
        LinearLayout optionContainer = basketStoreViewHolder.optionContainer;
        optionContainer.removeAllViews();

        if (options != null) {
            for (Option option : options) {
                View optionView = LayoutInflater.from(optionContainer.getContext())
                        .inflate(R.layout.item_option, optionContainer, false);

                TextView optionName = optionView.findViewById(R.id.option_name_textview);
                TextView optionPrice = optionView.findViewById(R.id.option_price_textview);

                optionName.setText(" ↳ " + option.getOptionName());
                optionPrice.setText(formatter.format(option.getOptionPrice()) + "원");

                optionContainer.addView(optionView);
            }
        }

        // 수량 조절 버튼
        basketStoreViewHolder.plusButton.setOnClickListener(v -> {
            int newAmount = item.getAmount() + 1;
            basketViewModel.updateAmountInFirestore(userId, item, newAmount);
        });
        basketStoreViewHolder.minusButton.setOnClickListener(v -> {
            // 수량이 1개면 수량을 줄일 수 없음
            if (item.getAmount() >= 2) {
                int newAmount = item.getAmount() - 1;
                basketViewModel.updateAmountInFirestore(userId, item, newAmount);
            } else Toast.makeText(this.context, "수량은 1개 이상 가능합니다.", Toast.LENGTH_LONG).show();
        });

        // 삭제 버튼
        basketStoreViewHolder.deleteButton.setOnClickListener(v -> {
            // 안내창
            new AlertDialog.Builder(this.context).setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        basketViewModel.deleteItemFromFirestore(userId, item);
                        Toast.makeText(this.context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("취소", null).show();
        });
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public int getTotalPrice() {
        return totalPriceAll;
    }

    // 새로운 데이터 갱신
    public static class BasketMenuDiffCallback extends DiffUtil.ItemCallback<BasketMenu> {
        @Override
        public boolean areItemsTheSame(@NonNull BasketMenu oldItem, @NonNull BasketMenu newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull BasketMenu oldItem, @NonNull BasketMenu newItem) {
            return oldItem.equals(newItem);
        }
    }

}