import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SpinnerAdapter extends Activity
{
ArrayList<String> name = new ArrayList<String>();
ArrayList<String> age = new ArrayList<String>();

@Override
public void onCreate(Bundle savedInstanceState)
{
super.onCreate(savedInstanceState);
setContentView(R.layout.customspinner);
for (int i =0 ;i<=2;i++){
name.add(""+i);
age.add(""+i);
}
Spinner mySpinner = (Spinner)findViewById(R.id.spinner);
mySpinner.setAdapter(new MyAdapter(this, R.layout.spinnerrow, name));
}

public class MyAdapter extends ArrayAdapter<String>
{

public MyAdapter(Context context, int textViewResourceId, ArrayList<String> objects)
{
super(context, textViewResourceId, objects);
}

@Override
public View getDropDownView(int position, View convertView,ViewGroup parent)
{
return getCustomView(position, convertView, parent);
}

@Override
public View getView(int position, View convertView, ViewGroup parent)
{
return getCustomView(position, convertView, parent);
}

public View getCustomView(int position, View convertView, ViewGroup parent)
{

LayoutInflater inflater=getLayoutInflater();
View row=inflater.inflate(R.layout.spinnerrow, parent, false);
TextView label=(TextView)row.findViewById(R.id.company);
label.setText(name.get(position));

TextView sub=(TextView)row.findViewById(R.id.sub);
sub.setText(age.get(position));


return row;
}
}
}