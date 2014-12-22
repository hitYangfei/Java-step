## Java LinkedHashMap实现剖析

LinkedHashMap是HashMap的一个子类，它在HashMap的基础上维护一个双向链表，从而实现按照使用要求获得一定的顺序。

LinkedHashMap有两种顺序，一种为inserted-order即插入顺序 一种为access-order即访问顺序

在LinkedHashMap put的时候 如果key存在则调用recordAccess()函数访问顺序

否则调用addEntry进行双向链表的建立

```Java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
{         
          
    private static final long serialVersionUID = 3801124242820219131L;
    private transient Entry<K,V> header;
    private final boolean accessOrder;
    private static class Entry<K,V> extends HashMap.Entry<K,V> {
      Entry<K,V> before, after;

    }
}
```
通过函数recordAccess来调整访问顺序，无论是put更新一个key还是get一个key都会引起access-order的变化，这其实就是一个LRU算法。

这个函数会将访问的元素remove掉然后插入到LinkedHashMap的头部，即header之前。
```Java
void recordAccess(HashMap<K,V> m) {
    LinkedHashMap<K,V> lm = (LinkedHashMap<K,V>)m;
    if (lm.accessOrder) {
        lm.modCount++;
        remove();
        addBefore(lm.header);
    }
}
```
在put时,LinkedHashMap重写的addEntry函数，在addEntry中通过createEntry函数新建一个entry插入到LinkedHashMap中
```Java
void createEntry(int hash, K key, V value, int bucketIndex) {
    HashMap.Entry<K,V> old = table[bucketIndex];
    Entry<K,V> e = new Entry<K,V>(hash, key, value, old);
    table[bucketIndex] = e;
    e.addBefore(header);
    size++;
}  
```
可以看到新的Entry元素首先会插入到LinkedHashMap的父类HashMap中的table中，然后插入自己的双链表中，并插入到header之前，所以插入是O（1）的

深入到addBefore可以看到LinkedHashMap是一个双向的循环链表，即头指针的after是尾元素，尾部元素的before是头指针
```Java
private void addBefore(Entry<K,V> existingEntry) {
    after  = existingEntry;
    before = existingEntry.before;
    before.after = this;
    after.before = this;
} 
```
初始化header.after = header.before = header 

>1 插入一个元素后e0后
     header.before = e0;e0.after = header       
    header<--->e0
>2 插入第二个元素e1后
     header.before = e1;e1.after = header
    header<--->e0<--->e1
>3 插入第三个元素e2后
     header.before = e2;e2.after = header
    header<--->e0<--->e1<--->e2

所以链表LinkedHashMap时，只需要变量header的after方向即为插入的元素顺序


知道了put的过程,get就十分简单了，如果制定了access-order在get的时候会调整双链表顺序，其余的与HashMap一致。

Hashtable与HashMap都是Map的一个实现，二者基本相同，不同如下
>1. Hashtable是线程安全的，HashMap不是,Hashtable对真个table进行上锁
>2. Hashtable不允许key为null ,HashMap允许
>3. put时，HashMap有自己的hash函数对key.hashcode进行二次hash以达到更好的hash效果，Hashtable则没有直接hash = key.hashcode()
>4. HashMap的initialCapacity必须为2^n且默认为16,而Hashtable则没有这个限制且默认为11但是而这的loadFactor默认都是0,75f
>5. 在进行动态扩展的时候即，HashMap的增长规则为*2,Hashtable为*2+1
>6. HashMap是AbstractMap的子类，而Hashtable是Dictionary的子类，原因猜测应该是一个是支持NULL的一个是不支持NULL的Map抽象。

  

### TreeMap 内部是一棵红黑树
```Java
static final class Entry<K,V> implements Map.Entry<K,V> {
  K key;          
  V value;  
  Entry<K,V> left = null;
  Entry<K,V> right = null;
  Entry<K,V> parent;
  boolean color = BLACK;
}
```

### weakHashMap

当外面没有key的引用时，weakHashMap会自动释放key
