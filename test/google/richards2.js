// Copyright 2007 Google Inc.
// All rights reserved.

var standalone = false;
try {
  document;
} catch(error) {
  standalone = true;
}

//(function () {

/**
 * JavaScript implementation of the OO Richards benchmark.
 *
 * The Richards benchmark simulates the task dispatcher of an
 * operating system.
 **/
function runRichards() {
  var count = 100000;
  
  var scheduler = new Scheduler();
//  scheduler.addIdleTask(ID_IDLE, 0, null, count);
  
  var queue = new Packet(null, ID_WORKER, KIND_WORK);
//  queue = new Packet(queue,  ID_WORKER, KIND_WORK);
  scheduler.addWorkerTask(ID_WORKER, 1000, queue);
  
  queue = new Packet(null, ID_DEVICE_A, KIND_DEVICE);
//  queue = new Packet(queue,  ID_DEVICE_A, KIND_DEVICE);
//  queue = new Packet(queue,  ID_DEVICE_A, KIND_DEVICE);
  scheduler.addHandlerTask(ID_HANDLER_A, 2000, queue);
//  
//  queue = new Packet(null, ID_DEVICE_B, KIND_DEVICE);
//  queue = new Packet(queue,  ID_DEVICE_B, KIND_DEVICE);
//  queue = new Packet(queue,  ID_DEVICE_B, KIND_DEVICE);
//  scheduler.addHandlerTask(ID_HANDLER_B, 3000, queue);
//  
  scheduler.addDeviceTask(ID_DEVICE_A, 4000, null);
//
//  scheduler.addDeviceTask(ID_DEVICE_B, 5000, null);
  
  var start = getTime();
  scheduler.schedule();
  var end = getTime();
  
  if (scheduler.queueCount == EXPECTED_QUEUE_COUNT
    && scheduler.holdCount == EXPECTED_HOLD_COUNT) {
    if (standalone) {
      print("Time (richards): " + (end - start) + " ms.");
    }
  } else {
    var msg = "Error during execution: queueCount = " + scheduler.queueCount + 
          ", holdCount = " + scheduler.holdCount + ".";
    if (standalone) {
      print(msg);
    } else {
      error(msg);
    }
  }
}

/**
 * These two constants specify how many times a packet is queued and
 * how many times a task is put on hold in a correct run of richards. 
 * They don't have any meaning a such but are characteristic of a
 * correct run so if the actual queue or hold count is different from
 * the expected there must be a bug in the implementation.
 **/
var EXPECTED_QUEUE_COUNT = 232625;
var EXPECTED_HOLD_COUNT = 93050;

function getTime() {
  //if ('now' in Date) {
  //  return Date.now();
  //} else {
  //  return (new Date).getTime();
  //}
  return new Date();
}

/**
 * A scheduler can be used to schedule a set of tasks based on their relative
 * priorities.  Scheduling is done by maintaining a list of task control blocks
 * which holds tasks and the data queue they are processing.
 * @constructor
 */
function Scheduler() {
  this.queueCount = 0;
  this.holdCount = 0;
  this.blocks = new Array/*<TaskControlBlock>*/(NUMBER_OF_IDS);
  this.list = null;
  this.currentTcb = null;
  this.currentId = null;
}

var ID_IDLE       = 0;
var ID_WORKER     = 1;
var ID_HANDLER_A  = 2;
var ID_HANDLER_B  = 3;
var ID_DEVICE_A   = 4;
var ID_DEVICE_B   = 5;
var NUMBER_OF_IDS = 6;

var KIND_DEVICE   = 0;
var KIND_WORK     = 1;

/**
 * Add an idle task to this scheduler.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 * @param {int} count the number of times to schedule the task
 */
Scheduler.prototype.addIdleTask = function (id, priority, queue, count) {
  this.addRunningTask(id, priority, queue, new IdleTask(this, 1, count));
};

/**
 * Add a work task to this scheduler.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 */
Scheduler.prototype.addWorkerTask = function (id, priority, queue) {
  this.addTask(id, priority, queue, new WorkerTask(this, ID_HANDLER_A, 0));
};

/**
 * Add a handler task to this scheduler.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 */
Scheduler.prototype.addHandlerTask = function (id, priority, queue) {
  this.addTask(id, priority, queue, new HandlerTask(this));
};

/**
 * Add a handler task to this scheduler.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 */
Scheduler.prototype.addDeviceTask = function (id, priority, queue) {
  this.addTask(id, priority, queue, new DeviceTask(this))
};

/**
 * Add the specified task and mark it as running.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 * @param {Task} task the task to add
 */
Scheduler.prototype.addRunningTask = function (id, priority, queue, task) {
  this.addTask(id, priority, queue, task);
  this.currentTcb.setRunning();
};

/**
 * Add the specified task to this scheduler.
 * @param {int} id the identity of the task
 * @param {int} priority the task's priority
 * @param {Packet} queue the queue of work to be processed by the task
 * @param {Task} task the task to add
 */
Scheduler.prototype.addTask = function (id, priority, queue, task) {
  dumpValue(this); // [@1.9]
  dumpValue(this.list); // Null|[@15.26, *15.26]
  this.currentTcb = new TaskControlBlock(this.list, id, priority, queue, task);
  dumpValue(this.currentTcb);// ???
  this.list = this.currentTcb;
  dumpValue(id);//???
  this.blocks[id] = this.currentTcb;
};

/**
 * Execute the tasks managed by this scheduler.
 */
Scheduler.prototype.schedule = function () {
  dumpValue(this); // [@1.9]
  dumpValue(this.list); // [@15.12, *15.12]
  this.currentTcb = this.list;
  while (this.currentTcb != null) {
    if (this.currentTcb.isHeldOrSuspended()) {
      dumpValue(this.currentTcb.link); // Undef|Null|[@1.62, *1.52, @1.52, *1.91, @1.86, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @15.26, @1.28, *15.26, *1.28]
      this.currentTcb = this.currentTcb.link;
    } else {
      dumpValue(this.currentTcb); //Undef|Null|Num|INF|[ARRAY_SPLICE, OBJECT_TOSTRING, ARRAY_JOIN, OBJECT_HASOWNPROPERTY, OBJECT_PROPERTYISENUMERABLE, @1.52, *1.91, *1.62, ARRAY_PUSH, ARRAY_SHIFT, OBJECT_VALUEOF, OBJECT_TOLOCALESTRING, ARRAY_POP, @1.33, @1.91, *1.86, @1.28, ARRAY_SLICE, ARRAY_TOSTRING, *1.28, ARRAY_UNSHIFT, ARRAY_REVERSE, ARRAY_CONCAT, OBJECT_ISPROTOTYPEOF, @1.62, *1.52, @1.86, *1.33, ARR...
      dumpValue(this.currentTcb.id); //ndef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
      this.currentId = this.currentTcb.id;
      dumpValue(this.currentTcb); //  Undef|Null|Num|INF|[ARRAY_SPLICE, OBJECT_TOSTRING, ARRAY_JOIN, *15.12, OBJECT_HASOWNPROPERTY, OBJECT_PROPERTYISENUMERABLE, @1.52, *1.91, *1.62, ARRAY_PUSH, ARRAY_SHIFT, OBJECT_VAL.....
      this.currentTcb = this.currentTcb.run();
    }
  }
};

/**
 * Release a task that is currently blocked and return the next block to run.
 * @param {int} id the id of the task to suspend
 */
Scheduler.prototype.release = function (id) {
  var tcb = this.blocks[id];
  if (tcb == null) return tcb;
  tcb.markAsNotHeld();
  if (tcb.priority > this.currentTcb.priority) {
    return tcb;
  } else {
    return this.currentTcb;
  }
};

/**
 * Block the currently executing task and return the next task control block
 * to run.  The blocked task will not be made runnable until it is explicitly
 * released, even if new work is added to it.
 */
Scheduler.prototype.holdCurrent = function () {
  this.holdCount++;
  this.currentTcb.markAsHeld();
  return this.currentTcb.link;
};

/**
 * Suspend the currently executing task and return the next task control block
 * to run.  If new work is added to the suspended task it will be made runnable.
 */
Scheduler.prototype.suspendCurrent = function () {
  this.currentTcb.markAsSuspended();
  return this.currentTcb;
};

/**
 * Add the specified packet to the end of the worklist used by the task
 * associated with the packet and make the task runnable if it is currently
 * suspended.
 * @param {Packet} packet the packet to add
 */
Scheduler.prototype.queue = function (packet) {
  dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  dumpValue(packet.id); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  dumpValue(this);//[GLOBAL, @1.9]  <=====
  dumpValue(this.blocks);//Undef|[A@3.5]
  var t = this.blocks[packet.id]; // <===============readAllProperties [A@3.5][UInt|[@1.33, GLOBAL, *1.33, @1.14]]=Undef|Num|INF|[ARRAY_SPLICE, ARRAY_UNSHIFT, ARRAY_REVERSE, OBJECT_TOST...
  if (t == null) return t;
  this.queueCount++;
  packet.link = null;
  dumpValue(this.currentId); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  packet.id = this.currentId;
  return t.checkPriorityAdd(this.currentTcb, packet);
};

/**
 * A task control block manages a task and the queue of work packages associated
 * with it.
 * @param {TaskControlBlock} link the preceding block in the linked block list
 * @param {int} id the id of this block
 * @param {int} priority the priority of this block
 * @param {Packet} queue the queue of packages to be processed by the task
 * @param {Task} task the task
 * @constructor
 */
function TaskControlBlock(link, id, priority, queue, task) {
  dumpValue(link); // Null|[@15.26, *15.26]
  dumpValue(id); // UInt
  this.link = link;
  this.id = id;
  this.priority = priority;
  this.queue = queue;
  this.task = task;
  if (queue == null) {
    this.state = STATE_SUSPENDED;
  } else {
    this.state = STATE_SUSPENDED_RUNNABLE;
  }
}

/**
 * The task is running and is currently scheduled.
 */
var STATE_RUNNING = 0;

/**
 * The task has packets left to process.
 */
var STATE_RUNNABLE = 1;

/**
 * The task is not currently running.  The task is not blocked as such and may
* be started by the scheduler.
 */
var STATE_SUSPENDED = 2;

/**
 * The task is blocked and cannot be run until it is explicitly released.
 */
var STATE_HELD = 4;

var STATE_SUSPENDED_RUNNABLE = STATE_SUSPENDED | STATE_RUNNABLE;
var STATE_NOT_HELD = ~STATE_HELD;

TaskControlBlock.prototype.setRunning = function () {
  this.state = STATE_RUNNING;
};

TaskControlBlock.prototype.markAsNotHeld = function () {
  this.state = this.state & STATE_NOT_HELD;
};

TaskControlBlock.prototype.markAsHeld = function () {
  this.state = this.state | STATE_HELD;
};

TaskControlBlock.prototype.isHeldOrSuspended = function () {
  return (this.state & STATE_HELD) != 0 || (this.state == STATE_SUSPENDED);
};

TaskControlBlock.prototype.markAsSuspended = function () {
  this.state = this.state | STATE_SUSPENDED;
};

TaskControlBlock.prototype.markAsRunnable = function () {
  this.state = this.state | STATE_RUNNABLE;
};

/**
 * Runs this task, if it is ready to be run, and returns the next task to run.
 */
TaskControlBlock.prototype.run = function () {
  var packet;
  dumpValue(this); // [ARRAY_SPLICE, OBJECT_TOSTRING, ARRAY_JOIN, *15.12, OBJECT_HASOWNPROPERTY, OBJECT_PROPERTYISENUMERABLE, *1.91, @1.52, ARRAY_PUSH, *1.62, ARRAY_SHIFT, OBJECT_VALUEOF, ARRAY_POP, OBJECT_TOLOCALESTRING, @15.12, @1.33, @1.91, *1.86 ...
  dumpValue(this.queue); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  if (this.state == STATE_SUSPENDED_RUNNABLE) {
    packet = this.queue;
    dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
    this.queue = packet.link;
    if (this.queue == null) {
      this.state = STATE_RUNNING;
    } else {
      this.state = STATE_RUNNABLE;
    }
  } else {
    packet = null;
  }
  dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  return this.task.run(packet);
};

/**
 * Adds a packet to the worklist of this block's task, marks this as runnable if
 * necessary, and returns the next runnable object to run (the one
 * with the highest priority).
 */
TaskControlBlock.prototype.checkPriorityAdd = function (task, packet) {
  if (this.queue == null) {
    this.queue = packet;
    this.markAsRunnable();
    if (this.priority > task.priority) return this;
  } else {
    this.queue = packet.addTo(this.queue);
  }
  return task;
};

TaskControlBlock.prototype.toString = function () {
  return "tcb { " + this.task + "@" + this.state + " }";
};

/**
 * An idle task doesn't do any work itself but cycles control between the two
 * device tasks.
 * @param {Scheduler} scheduler the scheduler that manages this task
 * @param {int} v1 a seed value that controls how the device tasks are scheduled
 * @param {int} count the number of times this task should be scheduled
 * @constructor
 */
function IdleTask(scheduler, v1, count) {
  this.scheduler = scheduler;
  this.v1 = v1;
  this.count = count;
}

IdleTask.prototype.run = function (packet) {
  dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  this.count--;
  if (this.count == 0) return this.scheduler.holdCurrent();
  if ((this.v1 & 1) == 0) {
    this.v1 = this.v1 >> 1;
    return this.scheduler.release(ID_DEVICE_A);
  } else {
    this.v1 = (this.v1 >> 1) ^ 0xD008;
    return this.scheduler.release(ID_DEVICE_B);
  }
};

IdleTask.prototype.toString = function () {
  return "IdleTask"
};

/**
 * A task that suspends itself after each time it has been run to simulate
 * waiting for data from an external device.
 * @param {Scheduler} scheduler the scheduler that manages this task
 * @constructor
 */
function DeviceTask(scheduler) {
  this.scheduler = scheduler;
  this.v1 = null;
}

DeviceTask.prototype.run = function (packet) {
  dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  if (packet == null) {
    if (this.v1 == null) return this.scheduler.suspendCurrent();
    var v = this.v1;
    this.v1 = null;
    dumpValue(v); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
    return this.scheduler.queue(v);
  } else {
    this.v1 = packet;
    return this.scheduler.holdCurrent();
  }
};

DeviceTask.prototype.toString = function () {
  return "DeviceTask";
};

/**
 * A task that manipulates work packets.
 * @param {Scheduler} scheduler the scheduler that manages this task
 * @param {int} v1 a seed used to specify how work packets are manipulated
 * @param {int} v2 another seed used to specify how work packets are manipulated
 * @constructor
 */
function WorkerTask(scheduler, v1, v2) {
  this.scheduler = scheduler;
  this.v1 = v1;
  this.v2 = v2;
}

WorkerTask.prototype.run = function (packet) {
  dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  if (packet == null) {
    return this.scheduler.suspendCurrent();
  } else {
    if (this.v1 == ID_HANDLER_A) {
      this.v1 = ID_HANDLER_B;
    } else {
      this.v1 = ID_HANDLER_A;
    }
    dumpValue(this.id); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
    packet.id = this.v1;
    packet.a1 = 0;
    for (var i = 0; i < DATA_SIZE; i++) {
      this.v2++;
      if (this.v2 > 26) this.v2 = 1;
      dumpValue(this); // [@11.15, @13.14, GLOBAL, *12.14, @10.14, *13.14, @12.14, *11.15]
      dumpValue(this.v2); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
	dumpValue(i); // ???
      packet.a2[i] = this.v2;
    }
    dumpValue(packet); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
    return this.scheduler.queue(packet);
  }
};

WorkerTask.prototype.toString = function () {
  return "WorkerTask";
};

/**
 * A task that manipulates work packets and then suspends itself.
 * @param {Scheduler} scheduler the scheduler that manages this task
 * @constructor
 */
function HandlerTask(scheduler) {
  this.scheduler = scheduler;
  this.v1 = null;
  this.v2 = null;
}

HandlerTask.prototype.run = function (packet) {
  dumpValue(packet);
  if (packet != null) {
    if (packet.kind == KIND_WORK) {
      this.v1 = packet.addTo(this.v1);
    } else {
      this.v2 = packet.addTo(this.v2);
    }
  }
  if (this.v1 != null) {
    var count = this.v1.a1;
    var v;
    if (count < DATA_SIZE) {
      if (this.v2 != null) {
        v = this.v2;
        this.v2 = this.v2.link;
        dumpValue(count);//???
        v.a1 = this.v1.a2[count];
        this.v1.a1 = count + 1;
        dumpValue(v); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
        return this.scheduler.queue(v);
      }
    } else {
      v = this.v1;
      this.v1 = this.v1.link;
      dumpValue(v); // Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
      return this.scheduler.queue(v);
    }
  }
  return this.scheduler.suspendCurrent();
};

HandlerTask.prototype.toString = function () {
  return "HandlerTask";
};

/* --- *
 * P a c k e t
 * --- */
 
var DATA_SIZE = 4;

/**
 * A simple package of data that is manipulated by the tasks.  The exact layout
 * of the payload data carried by a packet is not importaint, and neither is the
 * nature of the work performed on packets by the tasks.
 *
 * Besides carrying data, packets form linked lists and are hence used both as
 * data and worklists.
 * @param {Packet} link the tail of the linked list of packets
 * @param {int} id an ID for this packet
 * @param {int} kind the type of this packet
 * @constructor
 */
function Packet(link, id, kind) {
  dumpValue(link); //  Undef|Null|UInt|NaN|[@1.62, *1.52, @1.52, @1.86, *1.91, *1.62, *1.33, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, @1.81, *1.86, @1.28, *1.28]
  dumpValue(id); // UInt (consts)
  this.link = link;
  this.id = id;
  this.kind = kind;
  this.a1 = 0;
  this.a2 = new Array(DATA_SIZE);
}

/**
 * Add this packet to the end of a worklist, and return the worklist.
 * @param {Packet} queue the worklist to add this packet to
 */
Packet.prototype.addTo = function (queue) {
  this.link = null;
  if (queue == null) return this;
  var peek, next = queue;
  while ((peek = next.link) != null)
    next = peek;
  next.link = this;
  dumpValue(this);//[@1.62, *1.52, *1.91, @1.52, @1.86, *1.33, *1.62, @1.33, *1.81, GLOBAL, @1.91, @1.57, *1.57, *1.86, @1.81, @1.28, *1.28]
  return queue;
};

Packet.prototype.toString = function () {
  return "Packet";
};

if (standalone) {
  runRichards();
}

// })();
