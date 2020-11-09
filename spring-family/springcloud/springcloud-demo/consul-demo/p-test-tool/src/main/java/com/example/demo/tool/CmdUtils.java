package com.example.demo.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;

import com.example.demo.test.StreamManage;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import lombok.extern.slf4j.Slf4j;

/**
 * 启动服务和停止服务
 * 
 * @author sandy
 *
 */
@Slf4j
public class CmdUtils {
	private static AtomicBoolean error = new AtomicBoolean(false);

	// public static void main(String[] args){
	// CmdUtils.startApp("D:\\tmp\\consumer\\consul-consumer-demo");
	// try {
	// Thread.sleep(10000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// CmdUtils.stopApp("D:\\tmp\\consumer\\consul-consumer-demo");
	//
	// System.exit(0);
	// }

	/*
	 * 启动程序
	 */
	public static synchronized boolean startApp(String appParentAbsolute) {
		String osName = System.getProperty("os.name");
		String[] cmds = null;
		try {
			if (osName.toLowerCase().contains("windows")) { // windows 平台
				cmds = new String[] { "cd /d \"" + appParentAbsolute + File.separator + "script" + "\"",
						"start.bat>start.log" };
				exec4Windows(cmds);
			} else if (osName.toLowerCase().contains("linux")) { // linux平台
				cmds = new String[] { "cd \"" + appParentAbsolute + File.separator + "script" + "\"",
						"./start.sh>start.log" };
				exec4Linux(cmds);
			}
			return true;
		} catch (IOException e) {
			log.error("start app fail", e);
		} catch (InterruptedException e) {
			log.error("start app fail", e);
		} catch (IllegalAccessError e) {
			log.error("start app fail", e);
		}
		return false;
	}

	/*
	 * 停止程序
	 */
	public static synchronized boolean stopApp(String appParentAbsolute) {
		String osName = System.getProperty("os.name");
		String[] cmds = null;
		try {
			if (osName.toLowerCase().contains("windows")) { // windows 平台
				cmds = new String[] { "cd /d \"" + appParentAbsolute + File.separator + "script" + "\"",
						"start /b stop.bat>stop.log" };
				exec4Windows(cmds);
			} else if (osName.toLowerCase().contains("linux")) { // linux平台
				cmds = new String[] { "cd \"" + appParentAbsolute + File.separator + "script" + "\"",
						"./stop.sh>stop.log" };
				exec4Linux(cmds);
			}
			return true;
		} catch (IOException e) {
			log.error("stop app fail", e);
		} catch (InterruptedException e) {
			log.error("stop app fail", e);
		} catch (IllegalAccessError e) {
			log.error("stop app fail", e);
		}
		return false;
	}

	/**
	 * 停止程序
	 * 
	 * @param jarName
	 * @return
	 */
	@Deprecated
	public static synchronized boolean stopApp() {
		return !stop("cmd") && !stop("java.exe");
	}

	@Deprecated
	private static boolean stop(String cmd) {
		String[] cmds = new String[] { "tasklist|findstr " + cmd };
		try {
			error.set(false);
			String result = exec4Windows(cmds);
			if (StringUtils.isNoneBlank(result)) {
				List<String> lines = Splitter.on("\r\n").splitToList(result);
				lines.parallelStream().forEach(line -> {
					List<String> strs = Splitter.on(" ").splitToList(line);
					String pid = StringUtils.EMPTY;
					int count = 0;
					for (String str : strs) {
						if (StringUtils.isNotBlank(str)) {
							count++;
						}
						if (count == 2) {
							pid = str;
							break;
						}
					}
					if (StringUtils.isEmpty(pid)) {
						return;
					}
					String[] killCmds = new String[] { "taskkill /pid " + StringUtils.stripToEmpty(pid) + " /F" };
					try {
						String newResult = exec4Windows(killCmds);
						error.compareAndSet(true, false);
						log.info("stop result[{}],pid[{}]", newResult, pid);
					} catch (IOException e) {
						log.error("stop app fail", e);
						error.compareAndSet(false, true);
					} catch (InterruptedException e) {
						log.error("stop app fail", e);
						error.compareAndSet(false, true);
					}
				});
			}
		} catch (IOException e) {
			log.error("stop app fail", e);
			error.compareAndSet(false, true);
		} catch (InterruptedException e) {
			log.error("stop app fail", e);
			error.compareAndSet(false, true);
		}
		return error.get();
	}

	private static String exec4Windows(String[] commands) throws IOException, InterruptedException {
		String cmd = Joiner.on("&&").join(commands);
		log.info("exec cmd : {}", cmd);
		return exec("cmd /c " + cmd);
	}

	private static String exec4Linux(String[] commands) throws IOException, InterruptedException {
		String cmd = Joiner.on("&&").join(commands);
		log.info("exec cmd : {}", cmd);
		return exec(new String[]{"/bin/bash","-c",cmd});
	}

	private static String exec(String[] commandArray) throws IOException, InterruptedException {
		Process process;// Process可以控制该子进程的执行或获取该子进程的信息
		log.info("exec cmd : {}", Joiner.on(" ").join(commandArray));
		process = Runtime.getRuntime().exec(commandArray);// exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
		return execBase(process);
	}
	
	private static String exec(String commands) throws IOException, InterruptedException {
		Process process;// Process可以控制该子进程的执行或获取该子进程的信息
		log.info("exec cmd : {}", commands);
		process = Runtime.getRuntime().exec(commands);// exec()方法指示Java虚拟机创建一个子进程执行指定的可执行程序，并返回与该子进程对应的Process对象实例。
		return execBase(process);
	}

	private static String execBase(Process process) throws IOException, InterruptedException {
		// 下面两个可以获取输入输出流
		InputStream errorStream = process.getErrorStream();
		InputStream inputStream = process.getInputStream();

		String result = StringUtils.EMPTY;

		StreamManage stream = new StreamManage(inputStream, "normal");
		StreamManage errorStreamThread = new StreamManage(errorStream, "Error");
		stream.setName("normal");
		stream.start();
		errorStreamThread.setName("Error");
		errorStreamThread.start();

		int exitStatus = 0;
		exitStatus = process.waitFor();// 等待子进程完成再往下执行，返回值是子线程执行完毕的返回值
		// 第二种接受返回值的方法
		int i = process.exitValue(); // 接收执行完毕的返回值
		log.debug("i----" + i);

		if (exitStatus != 0) {
			log.error("exec cmd exitStatus {}", exitStatus);
		} else {
			log.debug("exec cmd exitStatus {}", exitStatus);
		}

		errorStreamThread.setEnd(true);
		stream.setEnd(true);
		process.destroy(); // 销毁子进程
		process = null;

		if (exitStatus != 0 || StringUtils.isNotEmpty(errorStreamThread.getResult())) {
			result = errorStreamThread.getResult();
			throw new IllegalAccessError("exe fail,message:" + result);
		} else if (StringUtils.isNotEmpty(stream.getResult())) {
			result = stream.getResult();
		}

		return result;
	}
}
