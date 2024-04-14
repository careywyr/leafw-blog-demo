package cn.leafw.distributelock.service;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * ZooKeeperLockDemoWatcher
 * @author <a href="mailto:wyr95626@95626.cn">CareyWYR</a>
 * @date 2020/8/26
 */
public class ZooKeeperLockDemoWatcher implements Watcher {

    private ZooKeeper zooKeeper;
    private int inventory;
    private final static String ROOT_LOCK_EXCLUSIVE = "/exclusive_lock";
    private final static String EXCLUSIVE_LOCK_NODE = "lock";
    private final static String NODE_LOCK_EXCLUSIVE = ROOT_LOCK_EXCLUSIVE + "/" + EXCLUSIVE_LOCK_NODE;
    private final static String ROOT_LOCK_SHARE = "/shared_lock";
    private final static String OPTIMISTIC_LOCK = "/optimistic_lock";

    public ZooKeeperLockDemoWatcher(int inventory) {
        try {
            this.inventory = inventory;
            zooKeeper = new ZooKeeper("localhost:2181", 10000, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建根节点
     *
     * @param rootPath 根节点路径
     */
    private synchronized void createRootNode(String rootPath) {
        try {
            Stat stat = zooKeeper.exists(rootPath, false);
            if (stat == null) {
                zooKeeper.create(rootPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 乐观锁
     * @param countDownLatch countDownLatch
     */
    public void optimisticLock(CountDownLatch countDownLatch){
        try {
            Stat stat = new Stat();
            byte[] data = zooKeeper.getData(OPTIMISTIC_LOCK, false, stat);
            int inventory = Integer.parseInt(new String(data));
            System.out.println(inventory);
            inventory--;
            zooKeeper.setData(OPTIMISTIC_LOCK, String.valueOf(inventory).getBytes(), stat.getVersion());
            countDownLatch.countDown();
            System.out.println("修改后inventory = " + inventory);
        } catch (KeeperException | InterruptedException e) {
            System.out.println("版本号不对，不可修改库存，尝试重新去扣库存");
            optimisticLock(countDownLatch);
        }
    }

//    /**
//     * 排他锁, 一开始没按照流程图的思路写的 = =
//     *
//     * @throws Exception Exception
//     */
//    public void exclusiveLock(CountDownLatch countDownLatch) throws Exception{
//        // 创建根节点，如果已经有了就不用执行这个方法了
//        createRootNode(ROOT_LOCK_EXCLUSIVE);
//        System.out.println(Thread.currentThread().getName() + "进来了");
//        try {
//            // 获取锁成功，库存-1
//            String lockPath = zooKeeper.create(NODE_LOCK_EXCLUSIVE, Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//            inventory--;
//            System.out.println("inventory: " + inventory + "; threadName" + Thread.currentThread().getName());
//            System.out.println("lockPath: " + lockPath + "; threadName" + Thread.currentThread().getName());
//            System.out.println(Thread.currentThread().getName() + "锁创建:" + lockPath);
//            // 成功减掉库存后释放锁
//            zooKeeper.delete(NODE_LOCK_EXCLUSIVE, -1);
//            System.out.println(Thread.currentThread().getName() + "锁释放");
//            countDownLatch.countDown();
//        } catch (KeeperException | InterruptedException e) {
//            System.out.println(Thread.currentThread().getName() + "争抢锁失败");
//            Stat exists = zooKeeper.exists(NODE_LOCK_EXCLUSIVE, watchedEvent -> {
//                System.out.println(Thread.currentThread().getName() + "监听----->" + watchedEvent.getType());
//                if (watchedEvent.getState().equals(Event.KeeperState.SyncConnected)) {
//                    // 如果监控到锁被释放，则继续尝试获取锁
//                    if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
//                        System.out.println(Thread.currentThread().getName() + "节点删除");
//                        try {
//                            exclusiveLock(countDownLatch);
//                        } catch (Exception exception) {
//                            exception.printStackTrace();
//                        }
//                    }
//                }
//            });
//            // 如果没有成功注册监听可能是因为别的线程已经释放了，所以要尝试去获取锁
//            if (null == exists) {
//                exclusiveLock(countDownLatch);
//            }
//        }
//    }

    /**
     * 排他锁，按照流程图思路写的
     * @param countDownLatch countDownLatch
     */
    public void exclusiveLock2(CountDownLatch countDownLatch) {
        System.out.println(Thread.currentThread().getName() + "进来了");
        // 创建监听
        Stat exists = null;
        try {
            exists = zooKeeper.exists(NODE_LOCK_EXCLUSIVE, watchedEvent -> {
                System.out.println(Thread.currentThread().getName() + "监听----->" + watchedEvent.getType());
                if (watchedEvent.getState().equals(Event.KeeperState.SyncConnected)) {
                    // 如果监控到锁被释放，则继续尝试获取锁
                    if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
                        System.out.println(Thread.currentThread().getName() + "节点删除");
                        exclusiveLock2(countDownLatch);
                    }
                }
            });
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        // 如果没有成功注册监听可能是因为别的线程已经释放了或者还没有线程创建节点，所以要尝试去获取锁
        if (null == exists) {
            try {
                String lockPath = zooKeeper.create(NODE_LOCK_EXCLUSIVE, Thread.currentThread().getName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                // 创建成功,执行业务逻辑
                inventory--;
                System.out.println("inventory: " + inventory + "; threadName" + Thread.currentThread().getName());
                System.out.println("lockPath: " + lockPath + "; threadName" + Thread.currentThread().getName());
                System.out.println(Thread.currentThread().getName() + "锁创建:" + lockPath);
                // 成功减掉库存后释放锁
                zooKeeper.delete(NODE_LOCK_EXCLUSIVE, -1);
                System.out.println(Thread.currentThread().getName() + "锁释放");
                countDownLatch.countDown();
            } catch (KeeperException | InterruptedException e) {
                // 获取锁失败就尝试再次获取
                System.out.println(Thread.currentThread().getName() + "争抢锁失败");
                exclusiveLock2(countDownLatch);
            }
        }

    }

    /**
     * 共享锁
     * @param countDownLatch countDownLatch
     * @param operationType  操作类型, W写，R读
     */
    public void sharedLock(CountDownLatch countDownLatch, String operationType) {
        // 创建根节点，如果已经有了就不用执行这个方法了
        createRootNode(ROOT_LOCK_SHARE);
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " 进来了, 操作类型是:" + operationType);
        try {
            // 创建临时有序节点,尾巴上面会自动跟上序号
            String lockPath = zooKeeper.create(ROOT_LOCK_SHARE + "/" + threadName + "-" + operationType + "-", threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(threadName + " 创建了节点, path = " + lockPath);
            acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
//            acquireLockNoHerdEffect(operationType, lockPath, countDownLatch);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 有羊群效应
     * @param operationType operationType
     * @param lockPath lockPath
     * @param countDownLatch countDownLatch
     * @throws KeeperException KeeperException
     * @throws InterruptedException InterruptedException
     */
    private void acquireLockWithHerdEffect(String operationType, String lockPath, CountDownLatch countDownLatch) throws KeeperException, InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.println("checkLock方法--->" + threadName + " 进来了, 操作类型是:" + operationType);
        // 获取子节点列表
        List<String> children = zooKeeper.getChildren(ROOT_LOCK_SHARE, false);
        // 节点从小到大排序
        List<String> sortedChildren = children.stream().sorted((o1, o2) -> {
            String o1Index = o1.substring(o1.lastIndexOf("-") + 1);
            String o2Index = o2.substring(o2.lastIndexOf("-") + 1);
            return Integer.parseInt(o1Index) - Integer.parseInt(o2Index);
        }).collect(Collectors.toList());
        // 如果是写请求，如果自己不是序号最小的子节点，那么就需要进入等待
        String minIndex = getLockPathIndex(sortedChildren.get(0));
        String thisIndex = getLockPathIndex(lockPath);
        if ("W".equals(operationType)) {
            // 如果当前序号是最小的，认为获取到了锁，可以执行业务
            if (minIndex.equals(thisIndex)) {
                inventory--;
                System.out.println(threadName + " 写请求，当前序号最小 ---> inventory = " + inventory);
                System.out.println(threadName + " 删除节点: " + lockPath);
                zooKeeper.delete(lockPath, -1);
                countDownLatch.countDown();
            } else {
                // 如果不是最小的，则等待锁
                System.out.println(threadName + " 写请求，当前序号不是最小 ---> inventory = " + inventory);
                acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
            }
        } else {
            // 如果是读请求，如果没有比自己序号小的子节点，或是所有比自己序号小的子节点都是读请求，那么表明自己已经成功获取到了共享锁，同时开始执行读取逻辑；如果比自己序号小的子节点中有写请求，那么就需要进入等待。
            if (minIndex.equals(thisIndex)) {
                System.out.println(threadName + " 读请求，序号最小 ----> inventory = " + inventory);
                System.out.println(threadName + " 删除节点: " + lockPath);
                zooKeeper.delete(lockPath, -1);
                countDownLatch.countDown();
            } else {
                // 查询比自己小的节点里是否有写节点
                long writeCount = sortedChildren.stream().filter(child -> Integer.parseInt(getLockPathIndex(child)) < Integer.parseInt(thisIndex))
                        .filter(child -> "W".equals(getOperationTypeFromPath(child))).count();
                // 如果有写的，等待
                if (writeCount > 0) {
                    System.out.println(threadName + " 读请求，比自己小的节点有写操作 ----> inventory = " + inventory);
                    acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
//                    zooKeeper.getChildren(ROOT_LOCK_SHARE, sharedLockWatch(operationType, lockPath, countDownLatch));
                } else {
                    System.out.println(threadName + " 读请求，比自己小的节点没有写操作 ----> inventory = " + inventory);
                    System.out.println(threadName + " 删除节点: " + lockPath);
                    zooKeeper.delete(lockPath, -1);
                    countDownLatch.countDown();
                }
            }
        }
    }


    /**
     * 无羊群效应
     * @param operationType operationType
     * @param lockPath lockPath
     * @param countDownLatch countDownLatch
     * @throws KeeperException KeeperException
     * @throws InterruptedException InterruptedException
     */
    private void acquireLockNoHerdEffect(String operationType, String lockPath, CountDownLatch countDownLatch) throws KeeperException, InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.println("checkLock方法--->" + threadName + " 进来了, 操作类型是:" + operationType);
        // 获取子节点列表
        List<String> children = zooKeeper.getChildren(ROOT_LOCK_SHARE, false);
        // 节点从小到大排序
        List<String> sortedChildren = children.stream().sorted((o1, o2) -> {
            String o1Index = o1.substring(o1.lastIndexOf("-") + 1);
            String o2Index = o2.substring(o2.lastIndexOf("-") + 1);
            return Integer.parseInt(o1Index) - Integer.parseInt(o2Index);
        }).collect(Collectors.toList());
        // 如果是写请求，如果自己不是序号最小的子节点，那么就需要进入等待
        String minNode = getLockPathIndex(sortedChildren.get(0));
        String thisNode = getLockPathIndex(lockPath);
        int thisIndex = -1;
        for (int i = 0; i < sortedChildren.size(); i++) {
            if (thisNode.equals(getLockPathIndex(sortedChildren.get(i)))) {
                thisIndex = i;
            }
        }
        // 比自己小的节点
        int lessIndex = thisIndex - 1;
        if ("W".equals(operationType)) {
            // 先对比自己小的节点挂上监听，挂不上说明比自己小的那个已经被删了
            if (lessIndex > -1 ) {
                String lessNode = sortedChildren.get(lessIndex);
                Stat exists = zooKeeper.exists(ROOT_LOCK_SHARE + "/ " + lessNode, watchedEvent -> {
                    if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
                        try {
                            acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (null == exists) {
                    try {
                        inventory--;
                        System.out.println(threadName + " 写请求，本来比自己小的节点已经删除了，所以当前序号最小 ---> inventory = " + inventory);
                        System.out.println(threadName + " 删除节点: " + lockPath);
                        zooKeeper.delete(lockPath, -1);
                        countDownLatch.countDown();
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                // 比自己小的节点不存在，直接执行业务
                inventory--;
                System.out.println(threadName + " 写请求，当前序号最小 ---> inventory = " + inventory);
                System.out.println(threadName + " 删除节点: " + lockPath);
                zooKeeper.delete(lockPath, -1);
                countDownLatch.countDown();
            }
        } else {
            // 查询比自己小的节点里是否有写节点
            List<String> lessWriteNodes = sortedChildren.stream().filter(child -> Integer.parseInt(getLockPathIndex(child)) < Integer.parseInt(thisNode))
                    .filter(child -> "W".equals(getOperationTypeFromPath(child))).collect(Collectors.toList());
            // 如果有写的，对比自己小的写请求注册监听, 挂不上可能是节点已经被删了
            if (lessWriteNodes.size() > 0) {
                System.out.println(threadName + " 读请求，比自己小的节点有写操作 ----> inventory = " + inventory);
                String lessWriteNode = lessWriteNodes.get(lessWriteNodes.size() - 1);
                Stat exists = zooKeeper.exists(lessWriteNode, watchedEvent -> {
                    if (Event.EventType.NodeDeleted.equals(watchedEvent.getType())) {
                        try {
                            acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
                        } catch (KeeperException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (null == exists) {
                    try {
                        System.out.println(threadName + " 读请求，序号最小 ----> inventory = " + inventory);
                        System.out.println(threadName + " 删除节点: " + lockPath);
                        zooKeeper.delete(lockPath, -1);
                        countDownLatch.countDown();
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                // 如果没有写节点就可以直接读即可
                System.out.println(threadName + " 读请求，序号最小 ----> inventory = " + inventory);
                System.out.println(threadName + " 删除节点: " + lockPath);
                zooKeeper.delete(lockPath, -1);
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 共享锁要用的监听
     * @param operationType 操作类型
     * @param lockPath 节点地址
     * @param countDownLatch  countDownLatch
     * @return Watcher
     */
    private Watcher sharedLockWatch(String operationType, String lockPath, CountDownLatch countDownLatch){
        return watchedEvent -> {
            if (Event.EventType.NodeChildrenChanged.equals(watchedEvent.getType())) {
                System.out.println(Thread.currentThread().getName()  + "触发监听, lockPath = " + lockPath);
                try {
                    System.out.println(Thread.currentThread().getName() + " 要尝试获取锁");
                    acquireLockWithHerdEffect(operationType, lockPath, countDownLatch);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * 获取节点的序号
     * @param path path
     * @return 序号
     */
    private String getLockPathIndex(String path) {
        return path.substring(path.lastIndexOf("-")  + 1);
    }

    /**
     * 根据节点获取操作类型
     * @param path path
     * @return 操作类型
     */
    private String getOperationTypeFromPath(String path) {
        return path.substring(path.indexOf("-") + 1, path.indexOf("-", path.indexOf("-") + 1));
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState().equals(Event.KeeperState.SyncConnected)) {
            System.out.println("连接成功");
        }
    }
}

