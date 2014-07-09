/**
 * 
 */
package tw.crazyma.loadjsontask;

import org.json.JSONObject;

/**
 * @author david
 *
 */
public interface OnParseJSONObjectListener {
	public Object onParse(JSONObject jsonObj);
}
