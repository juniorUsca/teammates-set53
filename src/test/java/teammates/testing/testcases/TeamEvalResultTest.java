package teammates.testing.testcases;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import teammates.TeamEvalResult;
import static teammates.TeamEvalResult.NA;
import static teammates.TeamEvalResult.NSB;
import static teammates.TeamEvalResult.NSU;
import static teammates.TeamEvalResult.replaceMagicNumbers;
import static teammates.api.Common.EOL;
import static teammates.TeamEvalResult.pointsToString;

import org.junit.Test;

public class TeamEvalResultTest {
	@Test
	// @formatter:off
	public void testCalculatePoints() {
		
		int[][] input = 
			{{ 100, 100, 100, 100 }, 
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 }};
		
		int[][] expected = 
			{{ 100, 100, 100, 100 }, 
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 
			 { 100, 100, 100, 100 },
			 
			 { 100, 100, 100, 100 }, 
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 }};
		
		verifyCalculatePoints(input, expected);
		
		int[][] input3 = 
			{{ 100, 100, 100, 100 }, 
			 { 110, 110, 110, 110 },
			 {  90,  90,  90,  90 },
			 {  10,  10,  10,  10 }};
		
		int[][] expected3 = 
			{{ 100, 100, 100, 100 }, 
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 
			 { 100, 100, 100, 100 },
			 
			 { 100, 100, 100, 100 }, 
			 { 110, 110, 110, 110 },
			 {  90,  90,  90,  90 },
			 {  10,  10,  10,  10 }};
		verifyCalculatePoints(input3, expected3);
		
		
		int[][] input2 = 
			{{ 100, 100, 100, 100 }, 
			 { 110, 110, 110, 110 },
			 {  90,  90,  90,  90 },
			 {  70,  80, 110, 120 }};
		
		int[][] expected2 = 
			{{ 100, 100, 100, 100 }, 
			 { 100, 100, 100, 100 },
			 { 100, 100, 100, 100 },
			 {  74,  84, 116, 126 },
			 
			 {  94, 97, 109, 100 },
			 
			 {  94,  97, 109, 100 }, 
			 { 103, 107, 120, 110 },
			 {  85,  87,  98,  90 },
			 {  89,  92, 104,  95 }};
		verifyCalculatePoints(input2, expected2);
		
		int[][] input4 = 
			{{ NSB, NSB, NSB, NSB }, 
			 { NSU, NSU, NSU, NSU },
			 { NSU, NSU, NSU, NSU },
			 { NSB, NSB, NSB, NSB }};
		
		int[][] expected4 = 
			{{ NSB, NSB, NSB, NSB }, 
			 { NSU, NSU, NSU, NSU },
			 { NSU, NSU, NSU, NSU },
			 { NSB, NSB, NSB, NSB },
			 
			 { NA, NA, NA, NA },
			 
			 { NA, NA, NA, NA }, 
			 { NA, NA, NA, NA },
			 { NA, NA, NA, NA },
			 { NA, NA, NA, NA }};

		verifyCalculatePoints(input4, expected4);
		
		int[][] input5 = 
			{{ 0, 0, 0, 0 }, 
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 }};
		
		int[][] expected5 = 
			{{ 0, 0, 0, 0 }, 
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 },
			 
			 { 0, 0, 0, 0 },
			 
			 { 0, 0, 0, 0 }, 
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 },
			 { 0, 0, 0, 0 }};
		verifyCalculatePoints(input5, expected5);
		
		int[][] input6 = 
			{{   0,   0,   0, NSU }, 
			 {   0,   0,   0, NSU },
			 { NSB, NSB, NSB, NSB },
			 {   0,   0, NSU, NSU }};
		
		int[][] expected6 = 
			{{   0,   0,   0, NSU }, 
			 {   0,   0,   0, NSU },
			 { NSB, NSB, NSB, NSB },
			 {   0,   0, NSU, NSU },
			 
			 { 0, 0, 0, NA },
			 
			 { 0, 0, 0, NA }, 
			 { 0, 0, 0, NA },
			 { 0, 0, 0, NA },
			 { 0, 0, 0, NA }};
		verifyCalculatePoints(input6, expected6);
		
		//only one person submitted
		int[][] input7 = 
			{{  25,  25,  75 }, 
			 { NSB, NSB, NSB },
			 { NSB, NSB, NSB }};
		
		int[][] expected7 = 
			{{  60,  60, 180 }, 
			 { NSB, NSB, NSB },
			 { NSB, NSB, NSB },
			 
			 {  NA,  NA,  NA },
			 
			 {  NA,  NA,  NA }, 
			 {  NA,  NA,  NA },
			 {  NA,  NA,  NA }};

		verifyCalculatePoints(input7, expected7);
		
		//only one person, submitted for self only
		int[][] input8 = 
			{{  25, NSU, NSU }, 
			 { NSB, NSB, NSB },
			 { NSB, NSB, NSB }};
		
		int[][] expected8 = 
			{{ 100, NSU, NSU }, 
			 { NSB, NSB, NSB },
			 { NSB, NSB, NSB },
			 
			 {  NA,  NA, NA },
			 
			 {  NA,  NA,  NA }, 
			 {  NA,  NA,  NA },
			 {  NA,  NA,  NA }};
		verifyCalculatePoints(input8, expected8);
		
		//two-person team
		int[][] input9 = 
			{{  50, 150 }, 
			 {  80, 80 }};
		
		int[][] expected9 = 
			{{  50, 150 }, 
			 { 100, 100 },
			 
			 {  NA,  NA },
			 
			 {  NA,  NA }, 
			 {  NA,  NA }};
		verifyCalculatePoints(input9, expected9);
	}

	private void verifyCalculatePoints(int[][] input, int[][] expected) {
		TeamEvalResult t = new TeamEvalResult(input);
		String actual = pointsToString(t.submissionValuesNormalized)
				+ "======================="+EOL
				+ Arrays.toString(t.perceivedForCoord) + EOL
				+ "=======================" + EOL
				+pointsToString(t.perceivedForStudent);
		actual = replaceMagicNumbers(actual);
		assertEquals(pointsToString(expected), actual);
	}
	
	@Test
	public void testNormalizeValues(){
		verifyNormalized(new double[] {}, new double[] {});
		verifyNormalized(new double[] {100}, new double[] {100});
		verifyNormalized(new double[] {100}, new double[] {50});
		verifyNormalized(new double[] {150,90,60}, new double[] {50,30,20});
		verifyNormalized(new double[] {0,0,0}, new double[] {0,0,0});
		verifyNormalized(new double[] {0,0,300},new double[] {0,0,100});
		verifyNormalized(new double[] {0,NA,200},new double[] {0,NA,100});
		verifyNormalized(new double[] {100,100,100},new double[] {110,110,110});
		verifyNormalized(new double[]{NA,NA},new double[]{NA,NA});
		verifyNormalized(new double[]{NSU,0,NSB},new double[]{NSU,0,NSB});
	}


	@Test 
	public void testExcludeSelfRatings(){
		
		double[][] input = 
			{{ 11, 12, 13, 14 }, 
			 { 21, 22, 23, 24 },
			 { 31, 32, 33, 34 },
			 { 41, 42, 43, 44 }};
		
		double[][] expected = 
			{{ NA, 12, 13, 14 }, 
			 { 21, NA, 23, 24 },
			 { 31, 32, NA, 34 },
			 { 41, 42, 43, NA }};
		assertEquals(pointsToString(expected),
				pointsToString(TeamEvalResult.excludeSelfRatings(input)));
	}
	
	@Test
	public void testAverageColumns(){
		double[][] input = 
			{{ 10, 20,  0, NA }, 
			 { 10, NA,  0, NA },
			 { 10, 20, NA, NA },
			 { 10, 20,  0, NA }};
		double[] expected = {10, 20, 0, NA};
		assertEquals(Arrays.toString(expected), 
				Arrays.toString(TeamEvalResult.averageColumns(input)));
		double[][] input2 = 
			{{ NA, NA, NA, NA }, 
			 { NA, NA, NA, NA },
			 { NA, NA, NA, NA },
			 { NA, NA, NA, NA }};
		double[] expected2 = {NA, NA, NA, NA};
		assertEquals(Arrays.toString(expected2), 
				Arrays.toString(TeamEvalResult.averageColumns(input2)));
		
	}
	
	@Test
	public void testSum(){
		assertEquals(6,TeamEvalResult.sum(new double[]{1,2,3}),0.001);
		assertEquals(0,TeamEvalResult.sum(new double[]{}),0.001);
		assertEquals(6,TeamEvalResult.sum(new double[]{NA, 2, 4}),0.001);
		assertEquals(0,TeamEvalResult.sum(new double[]{NA, 0, 0}),0.001);
		assertEquals(NA,TeamEvalResult.sum(new double[]{NA, NA, NA}),0.001);
	}
	
	@Test
	public void testCalculatePerceivedForStudent(){
		
		assertEquals(Arrays.toString(new int[]{}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{}, new double[]{})));
		
		assertEquals(Arrays.toString(new int[]{10}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{10}, new double[]{5})));
		
		assertEquals(Arrays.toString(new int[]{100,50,50}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{50,100,50}, new double[]{50,25,25})));
		
		assertEquals(Arrays.toString(new int[]{200,100,100}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{NA,150,50}, new double[]{50,25,25})));
		
		assertEquals(Arrays.toString(new int[]{NA,NA,NA}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{NA,NA,NA}, new double[]{NA,NA,NA})));
		
		assertEquals(Arrays.toString(new int[]{100,50,50}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{NA,NA,NA}, new double[]{100,50,50})));
		
		assertEquals(Arrays.toString(new int[]{100,100,400}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{50,150,NA}, new double[]{50,50,200})));
		
		assertEquals(Arrays.toString(new int[]{0,0,NA}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{0,0,NA}, new double[]{0,0,NA})));
		
		assertEquals(Arrays.toString(new int[]{NA,25,75}),
				Arrays.toString(TeamEvalResult.calculatePerceivedForStudent
						(new int[]{25,25,75}, new double[]{NA,50,150})));

	}
	
	@Test
	public void testIsSanitized(){
		assertEquals(true, TeamEvalResult.isSanitized(new int[]{}));
		assertEquals(true, TeamEvalResult.isSanitized(new int[]{1, 2, NA}));
		assertEquals(false, TeamEvalResult.isSanitized(new int[]{1, NSU, 2, NA}));
		assertEquals(false, TeamEvalResult.isSanitized(new int[]{NSB, 2, -1}));
	}
	
	@Test
	public void testPurgeValuesCorrespondingToSpecialValuesInFilter(){
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{}, 
				new double[]{}, new double[]{});
		
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{2.0}, 
				new double[]{1}, new double[]{2.0});
		
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, 3.0 }, 
				new double[]{1,2,3}, new double[]{1.0, 2.0, 3.0});
		
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, NA }, 
				new double[]{1,2,NA}, new double[]{1.0, 2.0, 3.0});
		
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, NA }, 
				new double[]{1,2,NSB}, new double[]{1.0, 2.0, 3.0});
		
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, NA }, 
				new double[]{1,2,NSU}, new double[]{1.0, 2.0, 3.0});
		
		//mix of special values
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, NA, 4.0, NA, 6.0, NA}, 
				new double[]{1,2,NSB,4,NSU,6,NA}, 
				new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
		
		// perceived values have NA
		verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
				new double[]{1.0, 2.0, NA, NA, NA, 6.0, NA}, 
				new double[]{1,2,NSB,4,NSU,6,NA}, 
				new double[]{1.0, 2.0, 3.0, NA, 5.0, 6.0});
	}
	// @formatter:on

	
	//--------------------------------------------------------------------
	private void verifyPurgeValuesCorrespondingToSpecialValuesInFilter(
			double[] expected, double[] filterArray, double[] valueArray) {
		assertEquals(Arrays.toString(expected), 
				Arrays.toString(TeamEvalResult.purgeValuesCorrespondingToSpecialValuesInFilter(
						filterArray, valueArray)));
	}
	
	private void verifyNormalized(double[] expected, double[] input) {
		assertEquals(Arrays.toString(expected), 
				Arrays.toString(TeamEvalResult.normalizeValues(input)));
	}
	

}