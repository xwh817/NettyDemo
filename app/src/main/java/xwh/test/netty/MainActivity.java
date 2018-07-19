package xwh.test.netty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import xwh.test.netty.server.NettyServerBootstrap;

public class MainActivity extends AppCompatActivity {

	private TextView mTextInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTextInfo = this.findViewById(R.id.text_info);
	}

	private NettyServerBootstrap mNettyServerBootstrap;
	public void startServer(View view) {
		mNettyServerBootstrap = new NettyServerBootstrap();
		mNettyServerBootstrap.bind();
	}

	public void gotoClient(View view) {
		startActivity(new Intent(this, ClientActivity.class));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
