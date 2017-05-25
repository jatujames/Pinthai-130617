package com.pinthai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class CallcenterActivity extends AppCompatActivity {
    TextView call_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callcenter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        call_text = (TextView) findViewById(R.id.call_text);
        call_text.setText("ติดต่อหน่วยแพทย์ฉุกเฉิน\n" +
                "    - 1554 > วชิรพยาบาล\n" +
                "    - 1669 > หน่วยงานรัฐ\n" +
                "    - 1691 > โรงพยาบาลตำรวจ\n" +
                "\n" +
                "แจ้งเหตุ / ติดต่อเจ้าหน้าที่ / สอบถามข้อมูล\n" +
                "    - 191   > เหตุด่วนเหตุร้าย\n" +
                "    - 199   > ดับเพลิง\n" +
                "    - 1130 > ไฟฟ้าขัดข้อง\n" +
                "    - 1146 > กรมทางหลวงชนบท\n" +
                "    - 1155 > ตำรวจท่องเที่ยว\n" +
                "    - 1192 > แจ้งรถหาย\n" +
                "    - 1193 > ตำรวจทางหลวง\n" +
                "    - 1195 > กองปราบปราม\n" +
                "    - 1197 > ข้อมูลการจราจร\n" +
                "    - 1199 > เหตุด่วนทางน้ำ\n" +
                "    - 1543 > สายตรงทางด่วน\n" +
                "\n" +
                "ศูนย์ช่วยเหลือ\n" +
                "    - 1137 > จส.ร้อย\n" +
                "    - 1255 > สถานีวิทยุจราจรเพื่อสังคม (FM 99.5)\n" +
                "    - 1644 > สวพ.91\n" +
                "    - 1677 > สถานีวิทยุชุมชนร่วมด้วยช่วยกัน (FM 96)\n");

    }
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
