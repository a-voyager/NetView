package top.wuhaojie.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final NetView netView = (NetView) findViewById(R.id.net_view);
        if (netView != null) {
            netView.addData("技能 A", 0.8f);
            netView.addData("技能 B", 0.6f);
            netView.addData("技能 C", 0.8f);
            netView.addData("技能 D", 0.6f);
            netView.addData("技能 E", 0.8f);
            netView.addData("技能 F", 0.6f);
        }

    }
}
