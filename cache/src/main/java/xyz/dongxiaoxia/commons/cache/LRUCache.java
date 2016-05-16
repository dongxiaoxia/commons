package xyz.dongxiaoxia.commons.cache;

import java.util.HashMap;

/**
 * LRU最近最少使用算法缓存类
 *
 * @author dongxiaoxia
 * @create 2016-05-15 20:59
 */
public class LRUCache<K,V> {

    private int cacheSize;//缓存容量
    private int currentSize;//当前容量
    private HashMap<K, CacheNode<K,V>> nodes;//缓存容器
    private CacheNode<K,V> head;//链表头
    private CacheNode<K,V> last;//链表尾

    class CacheNode<K,V> {
        CacheNode prev;//前一节点
        CacheNode next;//后一节点
        V value;//值
        K key;//键

        CacheNode() {
        }
    }

    //初始化缓存
    public LRUCache(int capacity){
        currentSize = 0;
        cacheSize = capacity;
        nodes = new HashMap<K, CacheNode<K,V>>(capacity);
    }

    public V get(K key){
        CacheNode<K,V> node ;
       return  (node = nodes.get(key)) == null ? null :node.value;
    }

    public void set(K key,V value){
        CacheNode node = nodes.get(key);
        //重复key
        if(node!=null){
            node.value = value;
            move(node);
            nodes.put(key,node);
        }else {
            //不重复,正常流程
            node = new CacheNode();
            if (currentSize>=cacheSize){//缓存已满，进行淘汰
                if (last!=null){
                    nodes.remove(last.key);
                }
                removeLast();//移除链表尾部并后移
            }else{
                currentSize++;
            }

            node.key = key;
            node.value = value;
            move(node);
            nodes.put(key,node);
        }
    }

    //移动链表节点至头部
    private void move(CacheNode cacheNode){
        if (cacheNode == head){
            return;
        }
        //链表前后节点
        if(cacheNode.prev!=null){
            cacheNode.prev.next = cacheNode.next;
        }
        if (cacheNode.next!=null){
            cacheNode.next.prev = cacheNode.prev;
        }
        //头尾节点
        if (last == cacheNode){
            last = cacheNode.prev;
        }
        if (head !=null){
            cacheNode.next = head;
            head.prev = cacheNode;
        }
        //移动后的链表
        head = cacheNode;
        cacheNode.prev = null;
        if (last == null){
            last = head;
        }
    }

    //移除指定缓存
    public void remove(int key){
        CacheNode cackeNode = nodes.get(key);
        if (cackeNode!=null){
            if (cackeNode.prev!=null){
                cackeNode.prev.next = cackeNode.next;
            }
            if (cackeNode.next!=null){
                cackeNode.next.prev = cackeNode.prev;
            }
            if (last == cackeNode){
                last = cackeNode.prev;
            }
            if (head == cackeNode){
                head = head.next;
            }
        }
    }

    //删除尾部的节点，即除去最近最久未使用数据
    private void removeLast(){
        if (last!=null){
            if (last.prev!=null){
                last.prev.next = null;
            }else {//空间大小为1的情况
                head = null;
            }
            last = last.prev;
        }
    }

    public void clear(){
        head = null;
        last = null;
    }
}
