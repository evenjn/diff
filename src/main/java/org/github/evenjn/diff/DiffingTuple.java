/**
 *
 * Copyright 2018 Marco Trevisan
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

import java.util.ArrayList;

import org.github.evenjn.knit.KnittingCursor;
import org.github.evenjn.knit.KnittingTuple;
import org.github.evenjn.lang.BasicEquivalencer;
import org.github.evenjn.lang.Equivalencer;
import org.github.evenjn.yarn.Cursor;
import org.github.evenjn.yarn.Tuple;

/**
 * 
 * <h1>DiffingTuple</h1>
 * 
 * <p>
 * A {@code DiffingTuple} wraps a {@link org.github.evenjn.yarn.Tuple Tuple} and
 * provides utility methods to compute distances and differences with respect to
 * other tuples.
 * </p>
 * 
 * <p>
 * Briefly, a {@code DiffingTuple} may be used in two ways:
 * </p>
 * 
 * <ul>
 * <li>As a simple tuple, invoking the
 * {@link org.github.evenjn.diff.DiffingTuple#get(int) get(int)} method;</li>
 * <li>As a value to be compared, invoking a comparison method such as
 * {@link #diff(Tuple)};</li>
 * </ul>
 * 
 * <h2>Methods of a DiffingTuple</h2>
 * 
 * <p>
 * Public instance methods of {@code DiffingTuple} fall into one of the
 * following three categories:
 * </p>
 * 
 * <ul>
 * <li>Object methods (inherited from {@link java.lang.Object Object})</li>
 * <li>Tuple methods ({@link #get(int)}, {@link #size()})</li>
 * <li>Comparison methods (listed below)</li>
 * </ul>
 *
 * <p>
 * Tuple comparison methods compare this tuple with one or more tuples. The
 * following methods are comparisons:
 * </p>
 * 
 * <ul>
 * <li>{@link #diff(Tuple)}</li>
 * <li>{@link #diff(Tuple, Equivalencer)}</li>
 * <li>{@link #distance_lcs(Tuple)}</li>
 * <li>{@link #distance_lcs(Tuple, Equivalencer)}</li>
 * <li>{@link #distance_levenshtein(Tuple)}</li>
 * <li>{@link #distance_levenshtein(Tuple, Equivalencer)}</li>
 * <li>{@link #longestCommonPrefix(Tuple)}</li>
 * <li>{@link #longestCommonPrefix(Tuple, Equivalencer)}</li>
 * <li>{@link #longestCommonSubtuple(Tuple)}</li>
 * <li>{@link #longestCommonSubtuple(Tuple, Equivalencer)}</li>
 * <li>{@link #longestCommonSubtupleIntersection(Cursor)}</li>
 * <li>{@link #longestCommonSubtupleIntersection(Cursor, Equivalencer)}</li>
 * <li>{@link #longestCommonSubtupleUnion(Cursor)}</li>
 * <li>{@link #longestCommonSubtupleUnion(Cursor, Equivalencer)}</li>
 * <li>{@link #longestCommonSuffix(Tuple)}</li>
 * <li>{@link #longestCommonSuffix(Tuple, Equivalencer)}</li>
 * </ul>
 *
 * <p>
 * This class is part of package {@link org.github.evenjn.diff Diff}.
 * </p>
 * 
 * @param <I>
 *          The type of elements accessible via this tuple.
 * @since 1.0
 */
public class DiffingTuple<I> implements
		Tuple<I> {

	private final static BasicEquivalencer<Object, Object> basic_equivalencer =
			new BasicEquivalencer<Object, Object>( );

	@SuppressWarnings("unchecked")
	private static <K, Y> Equivalencer<K, Y> private_equivalencer( ) {
		return (Equivalencer<K, Y>) basic_equivalencer;
	}

	/**
	 * <p>
	 * {@code wrap} returns a view of the elements in the argument
	 * {@link org.github.evenjn.yarn.Tuple Tuple}.
	 * </p>
	 * 
	 * @param <K>
	 *          The type of elements in the argument
	 *          {@link org.github.evenjn.yarn.Tuple Tuple}.
	 * @param tuple
	 *          A {@link org.github.evenjn.yarn.Tuple Tuple} of elements.
	 * @return A view of the elements in the argument
	 *         {@link org.github.evenjn.yarn.Tuple Tuple}.
	 * @since 1.0
	 */
	public static <K> DiffingTuple<K> wrap( Tuple<K> tuple ) {
		if ( tuple instanceof DiffingTuple ) {
			return (DiffingTuple<K>) tuple;
		}
		return new DiffingTuple<K>( tuple );
	}

	private final Tuple<I> wrapped;

	/**
	 * <p>
	 * {@code diff} returns an alignment of this tuple with the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes {@link DiffingTuple#diff(Tuple,Equivalencer)
	 * diff(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @return An alignment of this tuple with the argument tuple.
	 * @since 1.0
	 */
	public <Y> Iterable<DiffPair<I, Y>> diff( Tuple<Y> other ) {
		return diff( other, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code diff} returns an alignment of this tuple with the argument tuple,
	 * represented as a list of {@link org.github.evenjn.diff.DiffPair pairs}.
	 * </p>
	 * 
	 * <p>
	 * Each pair has two slots: one at the front, one at the back. At least one of
	 * the two slots is filled in with an element of the tuples.
	 * </p>
	 * 
	 * <p>
	 * Null counts as a value. When a tuple contains {@code null}, a slot will be
	 * filled with {@code null}.
	 * </p>
	 * 
	 * <p>
	 * Whenever the front slot of a pair
	 * {@linkplain org.github.evenjn.diff.DiffPair#hasFront( ) is filled in}, that
	 * slot contains an element of this tuple. That element may be {@code null}.
	 * </p>
	 * 
	 * <p>
	 * Whenever the back slot of a pair
	 * {@linkplain org.github.evenjn.diff.DiffPair#hasBack( ) is filled in}, that
	 * slot contains an element of the argument tuple. That element may be
	 * {@code null}.
	 * </p>
	 * 
	 * <p>
	 * Whenever both the front slot and the back slot
	 * {@linkplain org.github.evenjn.diff.DiffPair#hasBoth( ) are filled in}, the
	 * content of the front slot is equivalent (as specified by the argument
	 * {@code equivalencer}) to the content of the second slot. They may both be
	 * {@code null}.
	 * </p>
	 * 
	 * <p>
	 * The list is such that this tuple is equivalent to the tuple obtained by
	 * iterating over the list of pairs and collecting the elements in the front
	 * slot of each pair whenever that slot is filled in.
	 * </p>
	 * 
	 * <p>
	 * The list is such that the argument tuple is equivalent to the tuple
	 * obtained by iterating over the list of pairs and collecting the elements in
	 * the back slot of each pair whenever that slot is filled in.
	 * </p>
	 * 
	 * <p>
	 * The list is such that the tuple obtained by iterating over the list of
	 * pairs and collecting one of the elements of each pair whenever both slots
	 * are filled in is one of the <a href=
	 * "https://en.wikipedia.org/wiki/Longest_common_subsequence_problem">longest
	 * common subtuples</a> between this tuple and the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method's implementation is an adaptation of
	 * <a href="https://neil.fraser.name/" >Neil Fraser's work</a>.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return An alignment of this tuple with the argument tuple.
	 * @since 1.0
	 */
	public <Y> Iterable<DiffPair<I, Y>> diff(
			Tuple<Y> other,
			Equivalencer<I, Y> equivalencer ) {
		return new DiffIterable<I, Y>( this, other, equivalencer );
	}

	/**
	 * <p>
	 * {@code distance_lcs} returns the
	 * <a href= "https://en.wikipedia.org/wiki/Longest_common_subsequence_problem"
	 * >Longest Common Subsequence distance</a> between this tuple and the
	 * argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes {@link DiffingTuple#distance_lcs(Tuple,Equivalencer)
	 * distance_lcs(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @return The distance between this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int distance_lcs( Tuple<Y> other ) {
		return distance_lcs( other, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code distance_lcs} returns the
	 * <a href= "https://en.wikipedia.org/wiki/Longest_common_subsequence_problem"
	 * >Longest Common Subsequence distance</a> between this tuple and the
	 * argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method uses the argument {@code equivalencer} to decide whether an
	 * element of this tuple is equal to an element of the argument tuple.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return The distance between this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int distance_lcs( Tuple<Y> other,
			Equivalencer<I, Y> equivalencer ) {
		return distance( other, equivalencer, false );
	}

	/**
	 * <p>
	 * {@code distance_levenshtein} returns the
	 * <a href= "https://en.wikipedia.org/wiki/Levenshtein_distance" >Levenshtein
	 * distance</a> between this tuple and the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#distance_levenshtein(Tuple,Equivalencer)
	 * distance_levenshtein(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @return The distance between this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int distance_levenshtein( Tuple<Y> other ) {
		return distance_levenshtein( other, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code distance_levenshtein} returns the
	 * <a href= "https://en.wikipedia.org/wiki/Levenshtein_distance" >Levenshtein
	 * distance</a> between this tuple and the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method uses the argument {@code equivalencer} to decide whether an
	 * element of this tuple is equal to an element of the argument tuple.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return The distance between this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int distance_levenshtein( Tuple<Y> other,
			Equivalencer<I, Y> equivalencer ) {
		return distance( other, equivalencer, true );
	}

	/**
	 * <p>
	 * {@code get} returns the element mapped to the argument {@code index} by
	 * this tuple.
	 * </p>
	 * 
	 * @param index
	 *          A natural number. It must be non-negative.
	 * @return The element mapped to {@code index} by this tuple.
	 * @throws IllegalArgumentException
	 *           when {@code index} is negative, or when it is larger than or
	 *           equal to the size of this tuple.
	 * @since 1.0
	 */
	@Override
	public I get( int index ) {
		if ( index < 0 || index >= size( ) ) {
			throw new IllegalArgumentException( );
		}
		return wrapped.get( index );
	}

	/**
	 * <p>
	 * {@code longestCommonPrefix} returns the longest common prefix of this tuple
	 * and the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#longestCommonPrefix(Tuple,Equivalencer)
	 * longestCommonPrefix(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the other tuple.
	 * @param other
	 *          Another tuple
	 * @return The common prefix of this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int longestCommonPrefix( Tuple<Y> other ) {
		return longestCommonPrefix( other, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code longestCommonPrefix} returns the longest common prefix of this tuple
	 * and the argument tuple.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return The common prefix of this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int longestCommonPrefix( Tuple<Y> other,
			Equivalencer<I, Y> equivalencer ) {
		int n = Math.min( size( ), other.size( ) );
		for ( int i = 0; i < n; i++ ) {
			if ( !equivalencer.equivalent( get( i ), other.get( i ) ) ) {
				return i;
			}
		}
		return n;
	}

	/**
	 * <p>
	 * {@code longestCommonSubtuple} returns a longest common subsequence between
	 * this tuple and the argument tuple. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#longestCommonSubtuple(Tuple,Equivalencer)
	 * longestCommonSubtuple(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param tuple
	 *          Another tuple.
	 * @return a longest common subsequence between this tuple and the argument
	 *         tuple.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtuple(
			Tuple<Y> tuple ) {
		return longestCommonSubtuple( tuple, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code longestCommonSubtuple} returns a longest common subsequence between
	 * this tuple and the argument tuple. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * In general, there may be more than one longest common subsequences between
	 * two tuples.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param tuple
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return a longest common subsequence between this tuple and the argument
	 *         tuple.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtuple(
			Tuple<Y> tuple,
			Equivalencer<I, Y> equivalencer ) {
		ArrayList<I> result = new ArrayList<>( );
		for ( DiffPair<I, Y> bi : diff( tuple, equivalencer ) ) {
			if ( bi.hasBoth( ) ) {
				result.add( bi.front( ) );
			}
		}
		return KnittingTuple.wrap( result );
	}

	/**
	 * <p>
	 * {@code longestCommonSubtupleIntersection} returns the intersection of some
	 * of the longest common subsequences between this tuple and each tuple in the
	 * argument cursor. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#longestCommonSubtupleIntersection(Cursor,Equivalencer)
	 * longestCommonSubtupleIntersection(Cursor, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param masks
	 *          A cursor of tuples.
	 * @return the intersection of some longest common subsequences between this
	 *         tuple and each tuple in the argument cursor.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtupleIntersection(
			Cursor<? extends Tuple<Y>> masks ) {
		return longestCommonSubtupleIntersection( masks,
				private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code longestCommonSubtupleIntersection} returns the intersection of some
	 * of the longest common subsequences between this tuple and each tuple in the
	 * argument cursor. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * The intersection is defined here as the list of element of this tuple that
	 * appear in every longest common subsequences computed between this tuple and
	 * the other tuples.
	 * </p>
	 * 
	 * <p>
	 * In general, there may be more than one longest common subsequences between
	 * two tuples. This implementation does not guarantee that the intersection is
	 * the longest or the shortest intersection subsequence. However, it does
	 * guarantee that, for each element in the intersection, an equivalent element
	 * can be found in each of the other tuples.
	 * </p>
	 * 
	 * <p>
	 * In particular, this method does not guarantee that it returns the longest
	 * subsequence common to all tuples.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the mask tuples.
	 * @param masks
	 *          A cursor of tuples.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return the intersection of some longest common subsequences between this
	 *         tuple and each tuple in the argument cursor.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtupleIntersection(
			Cursor<? extends Tuple<Y>> masks,
			Equivalencer<I, Y> equivalencer ) {

		ArrayList<Boolean> keeps = new ArrayList<>( );
		for ( int i = 0; i < size( ); i++ ) {
			keeps.add( true );
		}

		for ( Tuple<Y> single_mask : KnittingCursor.wrap( masks ).once( ) ) {

			int j = 0;
			for ( DiffPair<I, Y> bi : diff( single_mask, equivalencer ) ) {
				if ( bi.hasFront( ) ) {
					if ( !bi.hasBoth( ) ) {
						keeps.set( j, false );
					}
					j++;
				}
			}
		}

		ArrayList<I> result = new ArrayList<>( );
		int j = 0;
		for ( Boolean keep : keeps ) {
			if ( keep ) {
				result.add( get( j ) );
			}
			j++;
		}
		return KnittingTuple.wrap( result );
	}

	/**
	 * <p>
	 * {@code longestCommonSubtupleUnion} returns the union of some of the longest
	 * common subsequences between this tuple and each tuple in the argument
	 * cursor. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#longestCommonSubtupleUnion(Cursor,Equivalencer)
	 * longestCommonSubtupleUnion(Cursor, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the mask tuples.
	 * @param masks
	 *          A cursor of tuples.
	 * @return the union of some longest common subsequences between this tuple
	 *         and each tuple in the argument cursor.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtupleUnion(
			Cursor<? extends Tuple<Y>> masks ) {
		return longestCommonSubtupleUnion( masks, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code longestCommonSubtupleUnion} returns the union of some of the longest
	 * common subsequences between this tuple and each tuple in the argument
	 * cursor. Elements are drawn from this tuple.
	 * </p>
	 * 
	 * <p>
	 * The union is defined here as the list of element of this tuple that appear
	 * in one or more of the longest common subsequences computed between this
	 * tuple and the other tuples.
	 * </p>
	 * 
	 * <p>
	 * In general, there may be more than one longest common subsequences between
	 * two tuples. This implementation does not guarantee that the union is the
	 * longest or the shortest union subsequence. However, it does guarantee that,
	 * for each element in the union, an equivalent element can be found in one of
	 * the other tuples.
	 * </p>
	 * 
	 * 
	 * @param <Y>
	 *          The type of elements in the mask tuples
	 * @param masks
	 *          A cursor of tuples.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return the union of some longest common subsequences between this tuple
	 *         and each tuple in the argument cursor.
	 * @since 1.0
	 */
	public <Y> Tuple<I> longestCommonSubtupleUnion(
			Cursor<? extends Tuple<Y>> masks,
			Equivalencer<I, Y> equivalencer ) {

		ArrayList<Boolean> keeps = new ArrayList<>( );
		for ( int i = 0; i < size( ); i++ ) {
			keeps.add( false );
		}

		for ( Tuple<Y> single_mask : KnittingCursor.wrap( masks ).once( ) ) {

			int j = 0;
			for ( DiffPair<I, Y> bi : diff( single_mask, equivalencer ) ) {
				if ( bi.hasFront( ) ) {
					if ( !bi.hasBoth( ) ) {
						keeps.set( j, false );
					}
					j++;
				}
			}
		}

		ArrayList<I> result = new ArrayList<>( );
		int j = 0;
		for ( Boolean keep : keeps ) {
			if ( keep ) {
				result.add( get( j ) );
			}
			j++;
		}
		return KnittingTuple.wrap( result );
	}

	/**
	 * <p>
	 * {@code longestCommonSuffix} returns the longest common suffix of this tuple
	 * and the argument tuple.
	 * </p>
	 * 
	 * <p>
	 * This method invokes
	 * {@link DiffingTuple#longestCommonSuffix(Tuple,Equivalencer)
	 * longestCommonSuffix(Tuple, Equivalencer)} using a
	 * {@link org.github.evenjn.lang.BasicEquivalencer BasicEquivalencer}.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the other tuple.
	 * @param other
	 *          Another tuple
	 * @return The common suffix of this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int longestCommonSuffix( Tuple<Y> other ) {
		return longestCommonSuffix( other, private_equivalencer( ) );
	}

	/**
	 * <p>
	 * {@code longestCommonPrefix} returns the longest common suffix of this tuple
	 * and the argument tuple.
	 * </p>
	 * 
	 * @param <Y>
	 *          The type of elements in the argument tuple.
	 * @param other
	 *          Another tuple.
	 * @param equivalencer
	 *          A system that can tell whether two objects are equivalent.
	 * @return The common suffix of this tuple and the argument tuple.
	 * @since 1.0
	 */
	public <Y> int longestCommonSuffix( Tuple<Y> other,
			Equivalencer<I, Y> equivalencer ) {
		int text1_length = size( );
		int text2_length = other.size( );
		int n = Math.min( text1_length, text2_length );
		for ( int i = 1; i <= n; i++ ) {
			if ( !equivalencer.equivalent( get( text1_length - i ),
					other.get( text2_length - i ) ) ) {
				return i - 1;
			}
		}
		return n;
	}

	/**
	 * <p>
	 * {@code size} returns the size of this tuple.
	 * </p>
	 * 
	 * @return The size of this tuple.
	 * 
	 * @since 1.0
	 */
	@Override
	public int size( ) {
		return wrapped.size( );
	}

	private DiffingTuple(Tuple<I> tuple) {
		this.wrapped = tuple;
	}

	private <Y> int distance( Tuple<Y> other,
			Equivalencer<I, Y> equivalencer, boolean allow_substitution ) {
		Tuple<I> s = this;
		Tuple<Y> t = other;

		int n = s.size( );
		int m = t.size( );

		if ( n == 0 ) {
			return m;
		}
		else
			if ( m == 0 ) {
				return n;
			}

		if ( n > m ) {
			// swap the input strings to consume less memory
			return DiffingTuple.wrap( other ).distance( this,
					equivalencer.swap( ), allow_substitution );
		}

		final int p[] = new int[n + 1];
		// indexes into strings s and t
		int i; // iterates through s
		int j; // iterates through t
		int upper_left;
		int upper;

		Y t_j; // jth character of t
		int cost_sub;
		int cost_base;

		for ( i = 0; i <= n; i++ ) {
			p[i] = i;
		}

		for ( j = 1; j <= m; j++ ) {
			upper_left = p[0];
			t_j = t.get( j - 1 );
			p[0] = j;

			for ( i = 1; i <= n; i++ ) {
				upper = p[i];

				// minimum of cell to the left+1, to the top+1, diagonally left and up
				// +cost
				cost_base = Math.min( p[i - 1] + 1, p[i] + 1 );
				if ( allow_substitution ) {
					cost_sub = equivalencer.equivalent( s.get( i - 1 ), t_j ) ? 0 : 1;
					p[i] = Math.min( cost_base, upper_left + cost_sub );
				}
				else {
					p[i] = cost_base;
				}
				upper_left = upper;
			}
		}

		return p[n];

	}

}
