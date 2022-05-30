package test;

import java.util.ArrayList;
import java.util.List;

import entity.EntityHelper;
import it.unisa.dia.gas.plaf.jpbc.util.Arrays;

public class TestEntityHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> testStringList = new ArrayList<String>();
		
		testStringList.add("string a");
		testStringList.add("String b");
		testStringList.add("string c");
		testStringList.add("String d");
		testStringList.add("string e");
		testStringList.add("String f");
		EntityHelper.printStringList(testStringList);
		byte[] testBytes = EntityHelper.stringList_to_bytes(testStringList);
		List<String> stringList = EntityHelper.bytes_to_stringList(Arrays.copyOfRange(testBytes, 4, testBytes.length));
		EntityHelper.printStringList(stringList);
	}

}
