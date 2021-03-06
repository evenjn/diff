package org.github.evenjn.diff;

/**
 * <p>
 * A {@code DiffPair} presents the result of a diff between two sequences of
 * objects.
 * </p>
 * 
 * <p>
 * A {@code DiffPair} holds two references to objects, referred to as the
 * <em>front</em> and the <em>back</em>. There are no restrictions on the
 * references. One or both references may be {@code null}, and both references
 * may point to the same object. The referred objects need not be immutable or
 * satisfy any particular constraint.
 * </p>
 * 
 * <p>
 * A {@code DiffPair} may have unset references. Client software must verify
 * that a slot is set before reading it. This is accomplished by invoking
 * {@link DiffPair#hasFront() hasFront()}, {@link DiffPair#hasBack() hasBack()},
 * or {@link DiffPair#hasBoth() hasBoth()} before invoking any of
 * {@link DiffPair#front() front()} or {@link DiffPair#back() back()}.
 * </p>
 * 
 * <p>
 * This class is part of package {@link org.github.evenjn.diff Diff}.
 * </p>
 *
 * @param <F>
 *          The type of the object in the <em>front</em> slot.
 * @param <B>
 *          The type of the object in the <em>back</em> slot.
 * @since 1.0
 */
public interface DiffPair<F, B> {

	/**
	 * @return The object in the <em>front</em> slot.
	 * @since 1.0
	 */
	F front( );

	/**
	 * @return The object in the <em>back</em> slot.
	 * @since 1.0
	 */
	B back( );

	/**
	 * @return {@code true} when the <em>front</em> slot is not empty.
	 *         {@code false} otherwise.
	 * @since 1.0
	 */
	boolean hasFront( );

	/**
	 * @return {@code true} when the <em>back</em> slot is not empty.
	 *         {@code false} otherwise.
	 * @since 1.0
	 */
	boolean hasBack( );

	/**
	 * @return {@code true} when both the <em>front</em> slot and the
	 *         <em>back</em> slot are not empty. {@code false} otherwise.
	 * @since 1.0
	 */
	boolean hasBoth( );
}
