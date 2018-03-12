package recycle;

import com.bmgsoftware.campmanageralpha.R;
import com.bmgsoftware.campmanageralpha.TestIntentFragment;
import com.bmgsoftware.campmanageralpha.R.layout;

import android.app.Activity;
import android.os.Bundle;

public class TestIntentDemo extends Activity {

	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.intenttest);

	    if (getFragmentManager().findFragmentById(android.R.id.content) == null)
	    {
	      getFragmentManager().beginTransaction()
	                          .add(android.R.id.content,
	                               new TestIntentFragment()).commit();
	    }
	  }
}
