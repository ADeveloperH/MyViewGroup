package project.hjking.cn.myviewgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMyScrollowView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setLitener();

    }

    private void initView() {
        btnMyScrollowView = (Button) findViewById(R.id.btn);
    }

    private void setLitener() {
        btnMyScrollowView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                startActivity(new Intent(this,MyScrollowViewActivity.class));
                break;
        }
    }
}
