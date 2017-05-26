package uk.gov.dvsa.mot.test.utility ;

import java.util.Collection ;

import org.hamcrest.BaseMatcher ;
import org.hamcrest.Description ;
import org.hamcrest.Matcher ;

/**
 * Contains custom matchers for use in the unit test assertions
 *
 */
public class Matchers
{
  public static <T> Matcher<Collection<T>> isEmpty()
  {
    return new EmptyCollectionMatcher<>() ;
  }

  public static <T> Matcher<Collection<T>> hasSize( int size )
  {
    return new CollectionSizeMatcher<>( size ) ;
  }

  /**
   * Matches an empty collection
   * 
   * @param <T>
   */
  public static class EmptyCollectionMatcher<T> extends BaseMatcher<Collection<T>>
  {

    @Override
    public boolean matches( Object item )
    {
      return ( ( item instanceof Collection<?> ) && ( (Collection<?>)item ).isEmpty() ) ;
    }

    @Override
    public void describeTo( Description description )
    {
      // TODO Auto-generated method stub
      description.appendText( "an empty collection" ) ;
    }
  }

  /**
   * Matches a collection of the specified size
   *
   * @param <T>
   */
  public static class CollectionSizeMatcher<T> extends BaseMatcher<Collection<T>>
  {
    private final int size ;

    CollectionSizeMatcher( int size )
    {
      this.size = size ;
    }

    @Override
    public boolean matches( Object item )
    {
      return ( ( item instanceof Collection<?> ) && ( (Collection<?>)item ).size() == size ) ;
    }

    @Override
    public void describeTo( Description description )
    {
      description.appendText( String.format( "collection with %d elements", size ) ) ;
    }
  }
}
