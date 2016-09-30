package com.and1.algorithm.Intersection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created on 26.09.16
 */
public class IntersectionGRAYTest
{
	private IntersectionGRAY classUnderTest;

	private float delta = 0.01f;

	@Before
	public void setUp() throws Exception
	{
		classUnderTest = new IntersectionGRAY();
	}

	@Test
	public void getMinimumSumWhenAllValuesZero() throws Exception
	{
		float[] histogramBasic = new float[]{0, 0};

		float[] histogramToCompare = new float[]{0, 0};

		float result = classUnderTest.getMinSumForGrayHistogram(histogramBasic, histogramToCompare);

		Assert.assertEquals(0f, result, delta);
	}

	@Test
	public void getMinimumSumWhenAllValuesThreeAndFiveBoth() throws Exception
	{
		float[] histogramBasic = new float[]{3, 5};

		float[] histogramToCompare = new float[]{3, 5};

		float result = classUnderTest.getMinSumForGrayHistogram(histogramBasic, histogramToCompare);

		Assert.assertEquals(8f, result, delta);
	}

	@Test
	public void getMinimumSumWhenAllValuesThreeAndFiveBothMirrored() throws Exception
	{
		float[] histogramBasic = new float[]{3, 5};

		float[] histogramToCompare = new float[]{5, 3};

		float result = classUnderTest.getMinSumForGrayHistogram(histogramBasic, histogramToCompare);

		Assert.assertEquals(6f, result, delta);
	}

}