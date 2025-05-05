package com.example.test3.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.AddressActivity;
import com.example.test3.Interface.ApiAddress;
import com.example.test3.Model.Address;
import com.example.test3.R;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context;
    private List<Address> addressList;
    private int selectedPosition = -1; // Vị trí của địa chỉ mặc định

    public AddressAdapter(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;

        // Tìm địa chỉ mặc định ban đầu
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).isDefault()) {
                selectedPosition = i;
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Address address = addressList.get(position);

        // Gán dữ liệu vào TextView
        holder.tvUserName.setText(address.getReceiverName());
        holder.tvPhoneNumber.setText(address.getPhoneNumber());
        holder.tvAddress.setText(address.getHouseNumber() + " " + address.getStreet() + ", "
                + address.getWard() + ", " + address.getDistrict() + ", " + address.getCity());

        // Xử lý chọn địa chỉ mặc định
        holder.rbSelectAddress.setChecked(position == selectedPosition);
        holder.rbSelectAddress.setOnClickListener(v -> {
            if (selectedPosition != position) {
                int previousPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);

                setDefaultAddress(address.getAddressId());
            }
        });

        // Xử lý sự kiện khi nhấn "Cập nhật"
        holder.tvUpdate.setOnClickListener(v -> {
            showUpdateDialog(address, position);
        });

        // Xử lý sự kiện khi nhấn "Xóa"
        holder.tvDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xóa địa chỉ")
                    .setMessage("Bạn có chắc chắn muốn xóa địa chỉ này?")
                    .setPositiveButton("Xóa", (dialog, which) -> deleteAddress(address.getAddressId(), position))
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        holder.rbSelectAddress.setOnClickListener(v -> {
            selectedPosition = position;
            setDefaultAddress(address.getAddressId());
            saveDefaultAddressToPrefs(address);// Gọi API cập nhật
            notifyDataSetChanged(); // Refresh danh sách để cập nhật trạng thái
        });

    }
    private void saveDefaultAddressToPrefs(Address address) {
        SharedPreferences prefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("defaultAddressId", address.getAddressId());
        editor.putString("defaultAddress", address.getHouseNumber() + " " + address.getStreet() + ", " +
                address.getWard() + ", " + address.getDistrict() + ", " + address.getCity());
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvPhoneNumber, tvAddress, tvUpdate, tvDelete;
        RadioButton rbSelectAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvUpdate = itemView.findViewById(R.id.tvUpdate);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            rbSelectAddress = itemView.findViewById(R.id.rbSelectAddress);
        }
    }

    private void deleteAddress(int id, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress apiAddress = retrofit.create(ApiAddress.class);
        Call<ResponseBody> call = apiAddress.deleteAddress(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    addressList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDefaultAddress(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress apiAddress = retrofit.create(ApiAddress.class);
        Call<ResponseBody> call = apiAddress.setDefaultAddress(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Địa chỉ mặc định đã được cập nhật!", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged(); // Cập nhật lại danh sách
                } else {
                    Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showUpdateDialog(Address address, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_update_address, null);
        builder.setView(view);

        EditText edtReceiverName = view.findViewById(R.id.edtReceiverName);
        EditText edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        EditText edtHouseNumber = view.findViewById(R.id.edtHouseNumber);
        EditText edtStreet = view.findViewById(R.id.edtStreet);
        EditText edtWard = view.findViewById(R.id.edtWard);
        EditText edtDistrict = view.findViewById(R.id.edtDistrict);
        EditText edtCity = view.findViewById(R.id.edtCity);
        Button btnUpdateAddress = view.findViewById(R.id.btnUpdateAddress);

        edtReceiverName.setText(address.getReceiverName());
        edtPhoneNumber.setText(address.getPhoneNumber());
        edtHouseNumber.setText(address.getHouseNumber());
        edtStreet.setText(address.getStreet());
        edtWard.setText(address.getWard());
        edtDistrict.setText(address.getDistrict());
        edtCity.setText(address.getCity());

        AlertDialog dialog = builder.create();
        dialog.show();

        btnUpdateAddress.setOnClickListener(v -> {
            String newReceiverName = edtReceiverName.getText().toString().trim();
            String newPhoneNumber = edtPhoneNumber.getText().toString().trim();
            String newHouseNumber = edtHouseNumber.getText().toString().trim();
            String newStreet = edtStreet.getText().toString().trim();
            String newWard = edtWard.getText().toString().trim();
            String newDistrict = edtDistrict.getText().toString().trim();
            String newCity = edtCity.getText().toString().trim();

            if (newReceiverName.isEmpty() || newPhoneNumber.isEmpty() || newHouseNumber.isEmpty() ||
                    newStreet.isEmpty() || newWard.isEmpty() || newDistrict.isEmpty() || newCity.isEmpty()) {
                Toast.makeText(context, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            address.setReceiverName(newReceiverName);
            address.setPhoneNumber(newPhoneNumber);
            address.setHouseNumber(newHouseNumber);
            address.setStreet(newStreet);
            address.setWard(newWard);
            address.setDistrict(newDistrict);
            address.setCity(newCity);

            updateAddress(address, position, dialog);
        });
    }

    private void updateAddress(Address address, int position, AlertDialog dialog) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/android_api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAddress apiAddress = retrofit.create(ApiAddress.class);
        Call<ResponseBody> call = apiAddress.updateAddress(
                address.getAddressId(),
                address.getReceiverName(),
                address.getPhoneNumber(),
                address.getHouseNumber(),
                address.getStreet(),
                address.getWard(),
                address.getDistrict(),
                address.getCity()
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    addressList.set(position, address);
                    notifyDataSetChanged();
                    dialog.dismiss();
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
