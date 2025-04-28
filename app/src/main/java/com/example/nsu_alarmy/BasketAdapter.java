/* activity_basket.xml의 RV 연결 */
package com.example.nsu_alarmy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nsu_alarmy.data.BasketMenu;
import com.example.nsu_alarmy.data.BasketViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private static final String tag = "BasketAdapter";

    private Context context;
    private BasketViewModel basketViewModel;
    private String userId;

    private List<String> storeList = new ArrayList<>(); // 가게 목록
    private Map<String, List<BasketMenu>> groupByStoreBasket; // 가게별 장바구니 목록

    private LifecycleOwner lifecycleOwner; // 데이터 생명 주기 관리


    /* 어댑터 초기화 */
    public BasketAdapter(String userId, Context context, BasketViewModel basketViewModel, LifecycleOwner lifecycleOwner) {
        this.userId = userId;
        this.context = context;
        this.basketViewModel = basketViewModel;
        this.lifecycleOwner = lifecycleOwner;

        // 데이터 변경 시 자동 업데이트
        basketViewModel.getFilteredList().observe(lifecycleOwner, filterMap -> {
            groupByStoreBasket = filterMap;
            storeList.clear();
            // 가게별로 필터링한 데이터 존재하면, store를 storeList에 저장
            if (filterMap != null) {
                storeList.addAll(filterMap.keySet());
            }
            notifyDataSetChanged();
        });
    }


    /* RecyclerView 형태 설정 */
    /* item_basket.xml 형태로 뷰 생성*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView storeName, waitingAmount;
        ImageView storeImage;
        RecyclerView storeBasketRecyclerView;

        public ViewHolder(View view) {
            super(view);
            storeImage = view.findViewById(R.id.store_image);
            storeName = view.findViewById(R.id.store_name_textview);
            waitingAmount = view.findViewById(R.id.waiting_amount);

            storeBasketRecyclerView = view.findViewById(R.id.rv_store_order);
        }
    }


    /* RecyclerView ViewHolder 설정 */
    @NonNull
    @Override // 뷰 생성
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_basket, parent, false);
        return new ViewHolder(view);
    }

    @Override // 데이터 연결
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String store = storeList.get(position);
        viewHolder.storeName.setText(store);

        // 이미지, 대기 순서
        if (store.equals("버거운버거")) {
            viewHolder.storeImage.setImageResource(R.drawable.burger_image);
            viewHolder.waitingAmount.setText("1" + "팀 대기 중..");
        } else if (store.equals("덮밥")) {
            viewHolder.storeImage.setImageResource(R.drawable.kakao_logo);
            viewHolder.waitingAmount.setText("2" + "팀 대기 중..");
        } else if (store.equals("태산김치찜")) {
            viewHolder.storeImage.setImageResource(R.drawable.naver_logo);
            viewHolder.waitingAmount.setText("3" + "팀 대기 중..");
        } else if (store.equals("숑숑돈가스")) {
            viewHolder.storeImage.setImageResource(R.drawable.toss_logo);
            viewHolder.waitingAmount.setText("4" + "팀 대기 중..");
        } else {
            Log.e(tag, "존재하지 않는 store 데이터");
        }

        List<BasketMenu> basketMenuListEachStore = groupByStoreBasket.get(store); // 해당 가게의 장바구니 목록

        // 내부 recyclerView 사이즈 조절
        viewHolder.storeBasketRecyclerView.setHasFixedSize(false);
        viewHolder.storeBasketRecyclerView.setNestedScrollingEnabled(false);
        // 내부 recyclerView 형태 조절
        viewHolder.storeBasketRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        // 내부 recyclerView(가게별 메뉴) 출력
        BasketStoreAdapter basketStoreAdapter = new BasketStoreAdapter(userId, context, basketViewModel);
        int menuAmount=basketMenuListEachStore.stream()
                        .mapToInt(BasketMenu::getAmount).sum();
        Log.d(tag, "가게명: " + store + " → 메뉴 수: " + (basketMenuListEachStore != null ? menuAmount : "null"));
        basketStoreAdapter.submitList(basketMenuListEachStore); // 내부 recyclerView 바인딩 _데이터 연결 오류 방지
        viewHolder.storeBasketRecyclerView.setAdapter(basketStoreAdapter);

        // 구분선
        if (viewHolder.storeBasketRecyclerView.getItemDecorationCount() == 0) {
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(

                    context, LinearLayoutManager.VERTICAL);
            viewHolder.storeBasketRecyclerView.addItemDecoration(dividerItemDecoration);
        }
    }

    @Override
    public int getItemCount() {
        return storeList.size(); // 저장된 가게 수
    }

}
