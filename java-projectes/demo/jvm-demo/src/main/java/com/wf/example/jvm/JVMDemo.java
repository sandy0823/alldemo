package com.wf.example.jvm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.extern.slf4j.Slf4j;

/**
 * 用来测试 jvm 进行 GC之后，内存是否归还给操作系统的验证
 * 
 * @author sandy
 *
 */
@Slf4j
public class JVMDemo {
	public static void main(String[] args) {
		@SuppressWarnings("rawtypes")
		List list = new CopyOnWriteArrayList<>();
		int count = 512;

		new Thread(() -> {
			try {
				for (int i = 0; i < 10; i++) {
					log.info(String.format("第%s次生产%s大小的对象", i, count));
					addObject(list, count);
					Thread.sleep(i * 10000);
				}
			} catch (Throwable ex) {
				log.error("execute fail", ex);
			}
		},"productThread").start();

		new Thread(() -> {
			while (true) {
				if (list.size() >= count) {
					log.info("清理List... 回收 jvm内存....");
					log.info("before");
					printJvmMemoryInfo();
					list.clear();
					System.gc();
					log.info("after");
					printJvmMemoryInfo();
				}
			}
		},"cleanThread").start();

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void addObject(List list, int count) {
		for (int i = 0; i < count; i++) {
			log.info("now list.size[{}]",list.size());
			OOMobject ooMobject = new OOMobject();
			list.add(ooMobject);
			try {
				Thread.sleep(100);
			} catch (Throwable ex) {
				log.error("add oomobject wrongly", ex);
			}
		}
//		log.info("add oomObject jvm memory...");
//		printJvmMemoryInfo();
	}

	public static class OOMobject {
		@SuppressWarnings("unused")
		private byte[] bytes = new byte[1024 * 1024];
	}

	private static void printJvmMemoryInfo() {
		long vmFree = 0;
		long vmUse = 0;
		long vmTotal = 0;
		long vmMax = 0;
		int byteToMb = 1024 * 1024;
		Runtime rt = Runtime.getRuntime();
		vmTotal = rt.totalMemory() / byteToMb;
		vmFree = rt.freeMemory() / byteToMb;
		vmMax = rt.maxMemory() / byteToMb;
		vmUse = vmTotal - vmFree;
		log.info("JVM内存情况：");
		log.info("Jvm 内存已用的空间为：" + vmUse + " MB");
		log.info("Jvm 内存空闲的空间为：" + vmFree + " MB");
		log.info("Jvm 总内存空间为：" + vmTotal + " MB");
		log.info("Jvm 总内存最大堆空间为：" + vmMax + " MB");
	}
}
