/**
 *
 * Copyright 2016 Marco Trevisan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.github.evenjn.diff;

import static org.junit.Assert.assertEquals;

import org.github.evenjn.knit.KnittingTuple;
import org.junit.Test;

public class DistanceTest {

	@Test
	public void test( ) {
		DiffingTuple<Integer> t1 = DiffingTuple.wrap( KnittingTuple.on( 1, 2, 3, 4 ) );

		assertEquals( "distance", 0,
				t1.distance_levenshtein( KnittingTuple.on( 1, 2, 3, 4 ) ) );
		assertEquals( "distance", 1,
				t1.distance_levenshtein( KnittingTuple.on( 1, 2, 3, 4, 5 ) ) );
		assertEquals( "distance", 2,
				t1.distance_levenshtein( KnittingTuple.on( 1, 2, 3, 4, 5, 5 ) ) );
		assertEquals( "distance", 3,
				t1.distance_levenshtein( KnittingTuple.on( 0, 2, 3, 4, 5, 5 ) ) );
		assertEquals( "distance", 4,
				t1.distance_levenshtein( KnittingTuple.on( 0, 2, 3, 1, 1, 4, 5 ) ) );
		assertEquals( "distance", 2,
				t1.distance_levenshtein( KnittingTuple.on( 2, 1, 3, 4 ) ) );
		assertEquals( "distance", 4,
				t1.distance_levenshtein( KnittingTuple.on( 4, 9, 9, 9 ) ) );

		/*
		DiffingTuple<Integer> t2 = DiffingTuple.wrap( KnittingTuple.on( 1, 2, 3, 4, 5 ) );
		assertEquals( "distance", 1,
				t1.distanceAll( KnittingTuple.on( KnittingTuple.on( 1, 2, 3, 5 ) ) ) );

		assertEquals( "distance", 1,
				t1.distanceAll( KnittingTuple.on( KnittingTuple.on( 1, 2, 3 ) ) ) );
		
		
		

		assertEquals( "distance", 0,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 1, 2, 3, 4 ) ) ));
		assertEquals( "distance", 1,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 1, 2, 3, 4, 5 ) ) ));
		assertEquals( "distance", 2,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 1, 2, 3, 4, 5, 5 ) ) ));
		assertEquals( "distance", 3,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 0, 2, 3, 4, 5, 5 ) ) ));
		assertEquals( "distance", 4,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 0, 2, 3, 1, 1, 4, 5 ) ) ));
		assertEquals( "distance", 2,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 2, 1, 3, 4 ) ) ));
		
		assertEquals( "distance", 4,
				t1.distanceAll( KnittingTuple.on(KnittingTuple.on( 4, 9, 9, 9 ) ) ));
		

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3 ),
				KnittingTuple.on( 1, 2, 3 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3 ),
				KnittingTuple.on( 1, 2, 4 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 4 ),
				KnittingTuple.on( 1, 2, 4 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2 ),
				KnittingTuple.on( 1, 2, 4 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 2, 3, 4 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 2, 3, 4 ),
				KnittingTuple.on( 2, 3, 4 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 3, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 2, 3, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 1, 3, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 1, 2, 3 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 3, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 2, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 1, 4 ) ) ) );

		assertEquals( "distance", 0, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4 ),
				KnittingTuple.on( 1, 2 ) ) ) );

		assertEquals( "distance", 1, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4, 5 ) ) ) );

		assertEquals( "distance", 2, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 1, 2, 3, 4 ) ) ) );

		assertEquals( "distance", 2, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4, 3, 4 ),
				KnittingTuple.on( 1, 2, 1, 2, 3, 4 ) ) ) );

		assertEquals( "distance", 2, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 3, 4 ),
				KnittingTuple.on( 1, 2, 1, 2, 3, 4 ) ) ) );

		assertEquals( "distance", 2, t1.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 3, 4 ),
				KnittingTuple.on( 1, 2 ) ) ) );

		assertEquals( "distance", 0, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 3, 4, 5 ),
				KnittingTuple.on( 1, 2, 3 ) ) ) );

		assertEquals( "distance", 1, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 7, 5 ),
				KnittingTuple.on( 1, 2, 3, 6, 5 ) ) ) );

		assertEquals( "distance", 2, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 0, 3, 7, 5 ),
				KnittingTuple.on( 1, 0, 3, 6, 5 ) ) ) );
		assertEquals( "distance", 3, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 0, 9, 7, 5 ),
				KnittingTuple.on( 1, 0, 8, 6, 5 ) ) ) );
		assertEquals( "distance", 3, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 0, 9, 7, 5 ),
				KnittingTuple.on( 1, 3, 8, 6, 5 ) ) ) );
		assertEquals( "distance", 1, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 4, 4, 5 ) ) ) );
		assertEquals( "distance", 2, t2.distanceAll( KnittingTuple.on(
				KnittingTuple.on( 1, 2, 3, 3, 4, 4, 5 ) ) ) );
				*/
	}

}
