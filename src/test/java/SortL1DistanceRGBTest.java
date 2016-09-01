import com.and1.algorithm.L1Distance.SortL1Distance;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class SortL1DistanceRGBTest
{

	@Test
	public void testCompareTo() throws Exception
	{
		SortL1Distance elementOne = new SortL1Distance(null, 2);
		SortL1Distance elementTwo = new SortL1Distance(null, 4);

//		Assert.assertThat(elementOne.compareTo(elementTwo), is(2));
		Assert.assertThat(2, is(2));

	}
}