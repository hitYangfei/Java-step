## Java HashMap 实现剖析

### Map接口
```Java
public interface Map<K,V> {
  int size();
  boolean isEmpty();
  boolean containsKey(Object key);
  boolean containsValue(Object value);
  V get(Object key);
  V put(K key, V value);
  V remove(Object key);
  void putAll(Map<? extends K, ? extends V> m);
  void clear();
  Set<K> keySet();
  Collection<V> values();
  Set<Map.Entry<K, V>> entrySet();
  interface Entry<K,V> {
    K getKey();
    V getValue();
    V setValue(V value);
    boolean equals(Object o);
    int hashCode();
  }
  boolean equals(Object o);
  int hashCode();
}
```



### HashMap

```Java
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
  static final int DEFAULT_INITIAL_CAPACITY = 16;
  static final int MAXIMUM_CAPACITY = 1 << 30;
  static final float DEFAULT_LOAD_FACTOR = 0.75f;
  transient Entry[] table;
  transient int size;
  int threshold;
  final float loadFactor;
  transient volatile int modCount;
}
```
>Entry[] table类型的成员，这个就是HashMap的Hash表

>Entry<K,V>是一个单链表，所以通过Entry的结构可以发现，HashMap的对于冲突是使用单链表解决的
```Java
static class Entry<K,V> implements Map.Entry<K,V>{
  final K key;
  V value;
  Entry<K,V> next;
  final int hash;
}
```

#### 1. put实现
```Java
public V put(K key, V value) {
  if (key == null)
    return putForNullKey(value);
  int hash = hash(key.hashCode());
  int i = indexFor(hash, table.length);
  for (Entry<K,V> e = table[i]; e != null; e = e.next) {
    Object k;
    if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
      V oldValue = e.value;
      e.value = value;
      e.recordAccess(this);
      return oldValue;
    }
  }

  modCount++;
  addEntry(hash, key, value, i);
  return null;
}
```
从代码中可以看出HashMap是允许key为NULL的，通过putForNullKey()函数实现，putForNullKey()函数代码如下
```Java
private V putForNullKey(V value) {
  for (Entry<K,V> e = table[0]; e != null; e = e.next) {
    if (e.key == null) {
      V oldValue = e.value;
      e.value = value;
      e.recordAccess(this);
      return oldValue;
    }
  }
  modCount++;
  addEntry(0, null, value, 0);
  return null;
}
```
HashMap对与key是NULL的<k,v>都存储在table[0]这个bucket中，recoredAccess()这个函数是Entry的一个函数，官方注释是对于每一个已经存在的key的value的改变都需要调用这个函数，HashMap没有实现这个函数，应该是交给具体的子类来实现子类的逻辑。addEntry()函数是put方法的核心辅助函数，下面会说道，再回到put函数

HashMap计算某个key的hash值的方法为：

*计算key.hashcode()

*将1步的结果传入HashMap自己的hash函数中求到最后的值

然后根据hash值计算元素应该put到的bucket的位置，计算方法为hash值对table.length取模。

根据bucket的值得到Entry链表，遍历链表如果找到key则更新value否则调用addEntry方法添加一个Entry，下面看一下这个函数
```Java
void addEntry(int hash, K key, V value, int bucketIndex) {
  Entry<K,V> e = table[bucketIndex];
  table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
  if (size++ >= threshold)
    resize(2 * table.length);
}
```
每一次添加一个新的Entry都直接添加到链表的头部。

如果size > threshold则进行resize hash表,threshold是HashMap的一个成员表示resize的阈值，threshold = table.length * loadFactor;loadFactor默认为0.75。
resize的策略为hash表扩展为原来的2倍。在resize过程中会将原来hash表中的所有元素转移到新的hash表中，通过transfer函数完成，看一下这个函数
```Java
void transfer(Entry[] newTable) {
  Entry[] src = table;
  int newCapacity = newTable.length;
  for (int j = 0; j < src.length; j++) {
    Entry<K,V> e = src[j];
    if (e != null) {
      src[j] = null;
      do {
        Entry<K,V> next = e.next;
        int i = indexFor(e.hash, newCapacity);
        e.next = newTable[i];
        newTable[i] = e;
        e = next;
      } while (e != null);
    }
  }
}
```
可以看到在transfer函数会对原来的hash表中的元素根据hash值重新计算bucket的位置，从而完成数据复制的过程。
#### 2.get实现
下面看一下get如果实现
```Java
public V get(Object key) {
  if (key == null)
    return getForNullKey();
  int hash = hash(key.hashCode());
  for (Entry<K,V> e = table[indexFor(hash, table.length)];
      e != null;
      e = e.next) {
    Object k;
    if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
      return e.value;
  }
  return null;
}
```
  有了put的基础，get就很好理解了，如果key是null，通过getForNullKey函数处理，与put过程中的putForNullKey对应，这个函数会对table[0]的链表进行遍历查找；否则按照put时候的bucket计算方法得到bucket的位置，然后链表链表即可，这里值得说一下的是判断相等的过程
```Java
e.hash == hash && ((k = e.key) == key || key.equals(k))
```
  这里看到如果两个key相等，那么这个两个key的hashcode以及equals函数都要相同，这符合这两个函数的含义。


  综上就是HashMap中get、put最重要的两个借口的实现，可以看到判断一个key是否在HashMap中，与这个key的hashcode 以及 equals方法都有关系，所以key最后是不可变的对象，如Java中基本类型的包装类Integer、String等。



  参考
  http://www.importnew.com/7099.html

