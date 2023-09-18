package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";

    // todoList를 담을 수 있는 ArrayList를 만든다.
    ArrayList<Note> items = new ArrayList<>();


    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);

        return new ViewHolder(itemView);
    } // CheckBox와 deleteButton을 포함한 아이템들을 띄울 수 있다.

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoteAdapter.ViewHolder holder, int position) {
        Note item = items.get(position);
        holder.setItem(item);
        holder.setLinearLayout(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutToDo;
        CheckBox checkBox;
        Button deleteButton;
        Context context;

        public ViewHolder(View itemView) {
            super(itemView);

            layoutToDo = itemView.findViewById(R.id.layoutToDo);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TODO = checkBox.getText().toString();
                    deleteToDo(TODO);
                    Toast.makeText(v.getContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                }

                // 삭제할 SQL문을 실행하는 메소드
                private void deleteToDo(String TODO) {
                    String deleteSQL = "delete from " + NoteDataBase.TABLE_NOTE + " where TODO = '" + TODO + "'";
                    NoteDataBase database = NoteDataBase.getInstance(context);
                    database.execSQL(deleteSQL);
                }
            });
        } // ViewHolder()

        // EditText에서 입력받은 checkBox의 텍스트를 checkBox의 Text에 넣을 수 있는 하는 메서드
        public void setItem(Note item) {
            checkBox.setText(item.getTodo());
        }
        // 아이템들을 담은 LinearLayout을 보여주게하는 메서드
        public void setLinearLayout(Note item) {
            layoutToDo.setVisibility(View.VISIBLE);
        }

    }

    // 배열에 있는 item 들을 가리키는 메서드들
    public void setItems(ArrayList<Note> items) {
        this.items = items;
    }
}

