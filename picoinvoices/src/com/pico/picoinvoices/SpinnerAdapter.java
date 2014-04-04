//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//public class SpinnerAdapter extends Activity {
//      String []data1={"Brainbitz:java","Brainbitz:Android","Brainbitz:Embedded Systems","Brainbitz:PHP"};
//      String []data2={"6month Course","3month Course","3month Course","3month Course"};
//      Integer[] images={R.drawable.java,R.drawable.android,R.drawable.emb,R.drawable.php};
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        Spinner mySpinner = (Spinner)findViewById(R.id.spinner1);
//        mySpinner.setAdapter(new MyAdapter(this, R.layout.spinner, data1));
//    }
//    public class MyAdapter extends ArrayAdapter<String>
//    {
//
//            public MyAdapter(Context context, int textViewResourceId,List<String> list) {
//                  super(context, textViewResourceId, list);
//                  // TODO Auto-generated constructor stub
//            }
//            @Override
//        public View getDropDownView(int position, View convertView,ViewGroup parent) {
//            return getCustomView(position, convertView, parent);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            return getCustomView(position, convertView, parent);
//        }
//
//        public View getCustomView(int position, View convertView, ViewGroup parent) {
//
//            LayoutInflater inflater=getLayoutInflater();
//            View row=inflater.inflate(R.layout.spinner_text_layout, parent, false);
//            TextView label=(TextView)row.findViewById(R.id.spinnerText1);
//            label.setText(data1[position]);
//
//            TextView sub=(TextView)row.findViewById(R.id.spinnerText2);
//            sub.setText(data2[position]);
//
//            ImageView icon=(ImageView)row.findViewById(R.id.imageView1);
//            icon.setImageResource(images[position]);
//
//            return row;
//            }
//       
//   }
//           
//     
//    }