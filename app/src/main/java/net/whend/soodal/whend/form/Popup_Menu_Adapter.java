package net.whend.soodal.whend.form;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.whend.soodal.whend.R;

/**
 * @author alessandro.balocco
 */
public class Popup_Menu_Adapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;

    public Popup_Menu_Adapter(Context context, boolean isGrid) {
        layoutInflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
                view = layoutInflater.inflate(R.layout.item_option_menu, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.text);
            viewHolder.text_desc = (TextView) view.findViewById(R.id.text_description);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.text.setText("수정하기");
                viewHolder.text_desc.setText("일정을 수정해 다시 올릴 수 있습니다.");
                break;
            case 1:
                viewHolder.text.setText("삭제하기");
                viewHolder.text_desc.setText("일정을 삭제할 수 있습니다.");
                break;
            default:
                viewHolder.text.setText("취소");
                viewHolder.text_desc.setText("");
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView text;
        TextView text_desc;
    }
}
