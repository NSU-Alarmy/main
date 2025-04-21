/* activiy_order_history.xml의 RV 연결 */
package com.example.nsu_alarmy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.compose.ui.BiasAlignment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nsu_alarmy.data.OrderData;
import com.example.nsu_alarmy.data.OrderMenu;
import com.example.nsu_alarmy.data.OrderViewModel;
import com.example.nsu_alarmy.data.StoreOrder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private static final String tag = "OrderAdapter";

    private String userId;
    private Context context;
    private OrderViewModel orderViewModel;
    private LifecycleOwner lifecycleOwner;

    private Map<String, OrderData> orderDataMap = new HashMap<>();
    private List<String> orderIdList;

    /* 어댑터 초기화 */
    public OrderAdapter(String userId, Context context, OrderViewModel orderViewModel, LifecycleOwner lifecycleOwner) {
        this.userId = userId;
        this.context = context;
        this.orderViewModel = orderViewModel;
        this.lifecycleOwner = lifecycleOwner;

        // 데이터 변경 시 자동 업데이트
        orderViewModel.getOrderDataMap().observe(lifecycleOwner, dataMap -> {
            orderDataMap = dataMap;
            Log.d(tag, "옵저브된 데이터: " + dataMap);
            orderIdList = new ArrayList<>(orderDataMap.keySet());
            Collections.sort(orderIdList, Collections.reverseOrder()); // 시간 내림차순으로 정리

            notifyDataSetChanged();
        });

    }

    /* RecyclerView 형태 설정 */
    /* item_order.xml 형태로 뷰 설정 */
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, complete;
        ImageView storeImage;
        TextView orderStore, orderMenu, totalPrice;

        public OrderViewHolder(View view) {
            super(view);
            orderDate = view.findViewById(R.id.dateTextView);
            complete = view.findViewById(R.id.statusTextView);
            storeImage = view.findViewById(R.id.storeImageView);
            orderStore = view.findViewById(R.id.storeTextView);
            orderMenu = view.findViewById(R.id.menuTextView);
            totalPrice = view.findViewById(R.id.priceTextView);
        }
    }

    /* RecyclerView ViewHolder 설정 */
    @NonNull
    @Override // 뷰 생성
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override // 데이터 연결
    public void onBindViewHolder(OrderViewHolder viewHolder, final int position) {
        // 주문 날짜
        String orderId = orderIdList.get(position);
        // 주문 내역 데이터
        OrderData orderData = orderDataMap.get(orderId);


        // 2024/04/18 형태로 변환
        String formatDate = orderId.substring(0, 4) + "/" + orderId.substring(4, 6) + "/" + orderId.substring(6, 8);
        viewHolder.orderDate.setText(formatDate + "  ");

        String firstMenu;

        if (orderData.getStoreMap() != null && !orderData.getStoreMap().isEmpty()) {
            Map<String, StoreOrder> storeOrder = orderData.getStoreMap();
            String firstStoreName = storeOrder.keySet().iterator().next(); // 첫 번째 가게 이름
            String firstStoreMenuList = storeOrder.get(firstStoreName).getMenuList().get(0).getMenu(); // 첫 번째 가게의 첫 번째 메뉴
            firstMenu = firstStoreMenuList;
            // 가게명
            int storeAmount = orderData.getStoreMap().size();
            if (storeAmount > 1) {
                int otherStore = storeAmount - 1;
                viewHolder.orderStore.setText(firstStoreName + " 외 " + otherStore + "개");
            } else viewHolder.orderStore.setText(firstStoreName);

            // 가게 이미지(가장 첫번째 가게 사진)
            if (firstStoreName.equals("버거운버거")) {
                viewHolder.storeImage.setImageResource(R.drawable.burger_image);
            } else if (firstStoreName.equals("덮밥")) {
                viewHolder.storeImage.setImageResource(R.drawable.kakao_logo);
            } else if (firstStoreName.equals("태산김치찜")) {
                viewHolder.storeImage.setImageResource(R.drawable.naver_logo);
            } else if (firstStoreName.equals("숑숑돈가스")) {
                viewHolder.storeImage.setImageResource(R.drawable.toss_logo);
            } else {
                Log.e(tag, "존재하지 않는 store 데이터");
            }
        } else {
            Log.e(tag, "StoreData의 데이터가 확인되지 않음");
            firstMenu = ""; // 오류 방지
        }


        orderViewModel.getTotalOfOrderData(orderId);
        // 메뉴 수
        orderViewModel.getTotalAmount().observe(lifecycleOwner, amountMap -> {
            Integer amount = amountMap.get(orderId);
            if (amount != null) {
                int otherAmount = amount - 1;
                viewHolder.orderMenu.setText(firstMenu + " 외 " + otherAmount + "개");
            }
        });

        // 가격
        orderViewModel.getTotalPrice().observe(lifecycleOwner, priceMap -> {
            Integer price = priceMap.get(orderId);
            if (price != null) {
                DecimalFormat formatter = new DecimalFormat("#,###");
                viewHolder.totalPrice.setText(formatter.format(price) + "원");
            }
        });

        // 주문 상태
        orderViewModel.getFinalComplete().observe(lifecycleOwner, completeMap -> {
            Boolean complete = completeMap.get(orderId);
            if (complete != null) {
                if (complete == true) {
                    viewHolder.complete.setText("[ 조리 완료 ]");
                } else viewHolder.complete.setText("[ 조리 중 ]");
            }
        });



//        // 클릭 시 OrderDetailActivity로 필요한 데이터만 전달
//        viewHolder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, OrderDetailActivity.class);
////            intent.putExtra("order_id", order.getId());
////            intent.putExtra("menu", order.getName());
////            intent.putExtra("date", order.getDate());
////            intent.putExtra("status", order.getStatus());
////            intent.putExtra("store", order.getStoreName());
////            intent.putExtra("waiting", order.getWaiting());
////            intent.putExtra("payment", order.getPayment());
//
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return orderIdList != null ? orderIdList.size() : 0;
    }
}

