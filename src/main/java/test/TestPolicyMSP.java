package test;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import policy_msp.BinNode;
import policy_msp.MSP_Builder;
import policy_msp.MSP_Listener;
import policy_msp.PolicyLexer;
import policy_msp.PolicyParser;

public class TestPolicyMSP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String policyString = "((a) and (b or c)) or ((d) and (a and b))";
		MSP_Builder util = new MSP_Builder();
		BinNode policy = util.createPolicy(policyString);

		Hashtable<String,List<Integer>> mono_span_prog = util.convert_policy_to_msp(policy);
		int num_cols = util.getLongestRow();
		//System.out.println(num_cols);
		
		Iterator entrySetIterator = mono_span_prog.entrySet().iterator();
		
		while(entrySetIterator.hasNext()) {
			System.out.println(entrySetIterator.next().toString());
		}
	}

}
