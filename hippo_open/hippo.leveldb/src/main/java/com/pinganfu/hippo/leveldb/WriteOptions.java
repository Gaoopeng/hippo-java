/**
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pinganfu.hippo.leveldb;


public class WriteOptions {
	private boolean sync;
	private boolean snapshot;
	private int bucket;
	private int bizApp;
	private short version;
	private long expireTime;
	
	public boolean sync() {
		return sync;
	}

	public WriteOptions sync(boolean sync) {
		this.sync = sync;
		return this;
	}

	public boolean snapshot() {
		return snapshot;
	}

	public WriteOptions snapshot(boolean snapshot) {
		this.snapshot = snapshot;
		return this;
	}

	public int bucket() {
		return bucket;
	}

	public WriteOptions bucket(int bucket) {
		this.bucket = bucket;
		return this;
	}

	public int bizApp() {
		return bizApp;
	}

	public WriteOptions bizApp(int bizApp) {
		this.bizApp = bizApp;
		return this;
	}

	public short version() {
		return version;
	}

	public WriteOptions version(short version) {
		this.version = version;
		return this;
	}

	public long expireTime() {
		return expireTime;
	}

	public WriteOptions expireTime(long expireTime) {
		if (expireTime > 0) {
			this.expireTime = System.currentTimeMillis() + expireTime * 1000;
		}  else {
		    //will not expire
			this.expireTime = -1;
		}
		return this;
	}
	
}
