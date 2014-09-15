package com.ThirtyNineEighty.Helpers;

import java.util.Arrays;
import java.util.Iterator;

public class VectorCollection<TVector extends Vector>
  implements Iterable<TVector>
{
  private static final int defaultCount = 16;

  private float[] array;
  private int vectorSize;

  private int size;

  public VectorCollection(int vectorSize)
  {
    this(vectorSize, defaultCount);
  }

  public VectorCollection(int vectorSize, int vectorsCount)
  {
    array = new float[vectorSize * vectorsCount];

    this.vectorSize = vectorSize;
  }

  public void add(TVector vector)
  {
    allocIfNeed();

    int position = size * vectorSize;

    for(int i = 0; i < vectorSize; i++)
      array[position + i] = vector.get(i);

    size++;
  }

  public void remove(int index)
  {
    checkRange(index);

    int size = this.size * vectorSize;
    index *= vectorSize;

    int numToMove = size - index - vectorSize;
    if (numToMove > 0)
      System.arraycopy(array, index + vectorSize, array, index, numToMove);

    this.size--;
  }

  public void getVector(TVector vector, int index)
  {
    checkRange(index);

    index *= vectorSize;
    for(int i = 0; i < vectorSize; i++)
      vector.set(i, array[index + i]);
  }

  public TVector getVector(int index)
  {
    checkRange(index);

    TVector vector = Vector.getInstance(vectorSize);

    index *= vectorSize;
    for(int i = 0; i < vectorSize; i++)
      vector.set(i, array[index + i]);

    return vector;
  }

  public boolean contains(TVector vector)
  {
    for (int i = 0; i < size; i++)
    {
      boolean has = true;

      for(int vecIndex = 0; vecIndex < vectorSize; vecIndex++)
        if (array[i * vectorSize + vecIndex] != vector.get(vecIndex))
        {
          has = false;
          break;
        }

      if (has)
        return true;
    }

    return false;
  }

  public void clear()
  {
    size = 0;
  }

  public int size()
  {
    return size;
  }

  private void allocIfNeed()
  {
    int neededLength = size * vectorSize + vectorSize;
    if (neededLength <= array.length)
      return;

    array = Arrays.copyOf(array, array.length * 2);
  }

  // <editor-fold desc="Iterable" >

  @Override
  public Iterator<TVector> iterator()
  {
    return new VectorCollectionIterator<TVector>(this, vectorSize);
  }

  public static class VectorCollectionIterator<TVector extends Vector>
    implements Iterator<TVector>
  {
    private VectorCollection<TVector> collection;
    private TVector currentVector;
    private int currentIndex;

    public VectorCollectionIterator(VectorCollection<TVector> c, int vectorSize)
    {
      currentIndex = 0;
      currentVector = Vector.getInstance(vectorSize);
      collection = c;
    }

    @Override
    public boolean hasNext()
    {
      return currentIndex < collection.size();
    }

    @Override
    public TVector next()
    {
      collection.getVector(currentVector, currentIndex);
      currentIndex ++;
      return currentVector;
    }

    @Override
    public void remove()
    {
      collection = null;
      currentVector = null;
      currentIndex = 0;
    }
  }

  // </editor-fold>

  private void checkRange(int index)
  {
    if (index < 0 || index >= size)
      throw new IndexOutOfBoundsException("Index should be at least 0 and less than " + size + ", found " + index);
  }
}
