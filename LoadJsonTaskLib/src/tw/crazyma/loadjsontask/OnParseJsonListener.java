/**
 * 
 */
package tw.crazyma.loadjsontask;

import org.json.JSONObject;

/**
 * @author david
 *
 */
public interface OnParseJsonListener {
	public Object onParse(JSONObject jsonObj);
}
