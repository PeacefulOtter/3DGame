package peacefulotter.game.Utils.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * An Object that can be listened
 * @param <T> : Object
 */
public class ListenedObject<T>
{
  // the listened attribute
  private T attr;
  // list of listeners, allow for multiple listeners on the same attribute
  private final List<ChangeListener<T>> listeners = new ArrayList<>();

  public ListenedObject() { }

  public ListenedObject( T attr )
  {
    this.attr = attr;
  }

  public T get() { return attr; }

  /**
   * Sends to each listener a "notification" that the attribute changed,
   * which is made by the call of their actionPerformed method
   * @param newAttr : the new attribute
   */
  public void set( T newAttr )
  {
    attr = newAttr;
    if ( !listeners.isEmpty() )
    {
      listeners.forEach( ( listener ) -> listener.actionPerformed( this, newAttr ) );
    }
  }

  /**
   * Adds a new listener to the listeners list
   * @param listener : a new ChangeListener
   */
  public void addListener( ChangeListener<T> listener )
  {
    listeners.add( listener );
  }
}
