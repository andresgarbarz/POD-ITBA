package ar.edu.itba.pod.concurrency.exercises.e1;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Basic implementation of {@link GenericService}.
 */
public  class GenericServiceImpl implements GenericService {

	private int visitCount = 0;
	private Queue<String> serviceQueue = new LinkedList<>();

    @Override
    public String echo(String message) {
		if (message == null) {
			return null;
		}
        return message;
    }

    @Override
    public String toUpper(String message) {
		if (message == null) {
			return null;
		}
		return message.toUpperCase();
    }

    @Override
    public void addVisit() {
		visitCount++;
    }

    @Override
    public int getVisitCount() {
        return visitCount;
    }

    @Override
    public boolean isServiceQueueEmpty() {
        return serviceQueue.isEmpty();
    }

    @Override
    public void addToServiceQueue(String name) {
		if (name == null) {
			throw new NullPointerException("Name cannot be null");
		}
        serviceQueue.add(name);
    }

    @Override
    public String getFirstInServiceQueue() {
		if (serviceQueue.isEmpty()) {
			throw new IllegalStateException("No one in queue");
		}
        return serviceQueue.poll();
    }
}
