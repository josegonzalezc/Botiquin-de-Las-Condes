package cl.uilabs.botiquindelascondes.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.util.Log;

import cl.uilabs.botiquindelascondes.R;
import cl.uilabs.botiquindelascondes.models.Medicamento;

/**
 * Created by jose on 17-11-17.
 */


public class MedicamentosAdapter extends BaseAdapter {

    public class ViewHolder {
        TextView medicamentoLaboratorio, medicamentoNombre,medicamentoPrecioNormal,medicamentoDescuento,medicamentoPrecioDescuento;


    }

    private final Context context;
    private final List<Medicamento> medicamentoList;

    private ArrayList<Medicamento> arraylist;

    public MedicamentosAdapter(Context context, List<Medicamento> medicamentoList) {
        this.context = context;
        this.medicamentoList = medicamentoList;
        arraylist = new ArrayList<Medicamento>();
        arraylist.addAll(medicamentoList);
    }
    @Override
    public int getCount() {
        if(medicamentoList != null) {
            return medicamentoList.size();
        }

        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("ADAPTER","ENTRE");

        View rowView = convertView;
        ViewHolder viewHolder;

        if (rowView == null) {
            LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.medicamento_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.medicamentoLaboratorio = rowView.findViewById(R.id.textview_laboratorio);
            viewHolder.medicamentoNombre = rowView.findViewById(R.id.textview_nombre);
            viewHolder.medicamentoPrecioNormal = rowView.findViewById(R.id.textview_precio_normal);
            viewHolder.medicamentoDescuento = rowView.findViewById(R.id.textview_descuento);
            viewHolder.medicamentoPrecioDescuento = rowView.findViewById(R.id.textview_precio_descuento);
            rowView.setTag(viewHolder);
            Log.i("ADAPTER","null");
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            Log.i("ADAPTER","converttag");
        }

        viewHolder.medicamentoLaboratorio.setText(medicamentoList.get(position).getLaboratorio() + "");
        viewHolder.medicamentoNombre.setText(medicamentoList.get(position).getNombre() + "");
        viewHolder.medicamentoPrecioNormal.setText(medicamentoList.get(position).getPrecio_normal() + "");
        viewHolder.medicamentoDescuento.setText(medicamentoList.get(position).getDescuento() + "");
        viewHolder.medicamentoPrecioDescuento.setText(medicamentoList.get(position).getPrecio_rebajado() + "");
        Log.i("ADAPTER","retorno rowView");

        return rowView;

    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        medicamentoList.clear();
        if (charText.length() == 0) {
            medicamentoList.addAll(arraylist);

        } else {
            for (Medicamento medicamento : arraylist) {
                if (charText.length() != 0 && medicamento.getNombre().toLowerCase(Locale.getDefault()).contains(charText)) {
                    medicamentoList.add(medicamento);
                } else if (charText.length() != 0 && medicamento.getLaboratorio().toLowerCase(Locale.getDefault()).contains(charText)) {
                    medicamentoList.add(medicamento);
                }
            }
        }
        notifyDataSetChanged();
    }
}
