package com.vailter.standard.learn.responsibilitychain.demo2;

/**
 * https://blog.csdn.net/kailuan2zhong/article/details/81670122
 * <p>
 * 在Tomcat中是通过容器组件对管道进行调用的，wrapper就是一种容器，因此可以通过如下方式进行调用：
 * wrapper.getPipeline().getFirst().invoke(request, response);
 */
public class StandardPipeline implements Pipeline {
    protected Valve basic = null;
    protected Valve first = null;

    @Override
    public Valve getBasic() {
        return (this.basic);
    }

    @Override
    public void setBasic(Valve valve) {
        Valve oldBasic = this.basic;
        if (oldBasic == valve) {
            return;
        }
        if (valve == null) {
            return;
        }
        Valve current = first;
        while (current != null) {
            if (current.getNext() == oldBasic) {
                current.setNext(valve);
                break;
            }
            current = current.getNext();
        }
        this.basic = valve;
    }

    /**
     * StandardPipeline是一个具有头尾指针的单向链表。first相当于头指针，basic相当于尾指针。addValve方法的作用是把管道中的各个阀门链接起来。
     *
     * @param valve
     */
    @Override
    public void addValve(Valve valve) {
        // Add this Valve to the set associated with this Pipeline
        if (first == null) {
            first = valve;
            valve.setNext(basic);
        } else {
            Valve current = first;
            while (current != null) {
                if (current.getNext() == basic) {
                    current.setNext(valve);
                    valve.setNext(basic);
                    break;
                }
                current = current.getNext();
            }
        }
    }

    @Override
    public Valve getFirst() {
        if (first != null) {
            return first;
        }
        return basic;
    }

    public static void main(String[] args) {
        Pipeline pipeline = new StandardPipeline();
        pipeline.addValve(new MyValve());
        pipeline.getFirst().invoke(new Request(), new Response());
    }
}
