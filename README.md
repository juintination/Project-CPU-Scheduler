# Project-CPU-Scheduler
Implementation of CPU Scheduler (FCFS, SJF, RR)

---

## 프로젝트 개요

- CPU scheduling 알고리즘에 따라 여러 가지 성능수치가 어떻게 달라지는가를 관 찰하기 위한 시뮬레이션
- Scheduling Algorithms : FCFS, SJF, RR(with quantum 1, 10, 100)
- Parameters : 각 process에 대해 그 process가 도착한 시각 A, 그 process가 종료될 때까지 필요로 하는 총 CPU time C,
CPU burst time은 0과 어떤 수 B 사이의 uniformly distributed random integer, IO burst time은 0과 어떤 수 IO 사이의 uniformly distributed random integer,
프로세스는 이 4개의 파라미터 (A, C, B, IO) 에 의해 정의됨. 숫자들의 단위는 time unit임.


프로그램은 n개의 프로세스들을 기술한 파일을 읽어 들인 후 그 n개의 process가 모두 끝날 때까지 그 process들을 시뮬레 이션 해야하며
모든 process의 수행이 끝나면 사용된 CPU scheduling 알고리즘, 사용된 파라미터 (예: Round Robin방식에서 사용되는 quantum),
simulate된 process의 수 등을 출력하고, 그 다음 각 process에 대해 다음과 같은 내용을 출력한다.

- (A, C, B, IO)
- Finishing Time(그 process가 끝난 시간)
- Turnaround Time (= finishing time - A)
- CPU time (그 process가 running state에 있었던 시간의 총합)
- I/O time (그 process가 blocked state에 있었던 시간의 총합)
- Waiting time(그 process가 ready state에 있었던 시간의 총합)
- (마지막 3가지의 시간의 합은 Turnaround time과 같아야 함)

이어서 다음의 summary data를 출력해야 함.
- Finishing time (모든 process가 다 끝난 시각)
- CPU utilization (Percentage of time some job is running)
- I/O utilization (Percentage of time some job is blocked)
- Throughput in processes completed per hundred time units
- Average turnaround time
- Average waiting time

testcase의 예는 다음과 같다.

- 5 (0 200 3 3)(0 500 9 3)(0 500 20 30)(100 100 1 0)(100 500 100 3)

(첫 번째 프로세스 - 도착시간: 0, 이 프로세스가 종료될 때까지 필요로 하는 총 CPU시간: 200,
이 프로세스의 각 CPU burst time: 0과 3사이의 random한 정수 값, 이 프로세스의 각 IO burst time: 0과 3 사이의 random한 정수 값)

---

## 목차

- [사용 방법](#사용-방법)
- [클래스 다이어그램](#클래스-다이어그램)
- [스케줄링 알고리즘 설명](#스케줄링-알고리즘-설명)
- [테스트 케이스](#테스트-케이스)

---

## 사용 방법

-   jdk 11 버전으로 작성되었으며 추가적인 라이브러리 설치는 필요하지 않음
-   Client.java : 테스트 케이스를 키보드로 입력, 스케줄링 알고리즘은 코드에서 수정하여야 함
-   Sched.java : 다음과 같은 코드를 terminal에 입력해야 함

**cd** _디렉터리명_/Project-CPU-Scheduler/src/

**javac** Sched.java

**java** Sched _텍스트파일명.txt_ _알고리즘명_ [quantum]

---

## 클래스 다이어그램

<img width="854" alt="스크린샷 2023-06-28 오후 4 52 23" src="https://github.com/juintination/Project-CPU-Scheduler/assets/89019601/e65035a7-a3b8-4242-89e6-30eb88ff3e59">

---

## 스케줄링 알고리즘 설명

프로세스는 실행 중인 프로그램으로 운영 체제에서 작업 단위로 프로세스가 태스크를 수행하려면 특정 리소스가 필요하다.(CPU 시간, 메모리, 파일, I/O 장치 등)

![프로세스 상태도](https://github.com/juintination/Project-CPU-Scheduler/assets/89019601/4decc375-3e3f-46ae-b680-19c6b0a231ed)


-   Process State
-   New: 프로세스가 새롭게 생성됨
-   Running: 명령이 실행되고 있음
-   Waiting: 프로세스가 일부 이벤트(I/O 완료 또는 신호 수신 등의 작업)가 발생하기를 기다리는 중임
-   Ready: 프로세스가 프로세서에 할당되기를 기다리고 있음
-   Terminated: 프로세스 실행이 완료되었음

![pcb](https://github.com/juintination/Project-CPU-Scheduler/assets/89019601/1217f7f6-8368-4270-ad11-187266474544)


각 프로세스는 운영 체제에서 PCB(프로세스 제어 블록)로 표시되며 PCB는 다음과 같은 관련된 많은 정보를 포함하고 있음

-   Process state
-   Program counter
-   CPU registers
-   CPU-scheduling information
-   Memory-management information
-   Accounting information
-   I/O status information


![queueing diagram](https://github.com/juintination/Project-CPU-Scheduler/assets/89019601/d43f9fa0-e029-447f-bc09-c9d14d08e915)

  
Scheduling Queues:

-   프로세스가 시스템에 진입하면 ready queue에 배치됨
-   특정 이벤트가 발생하기를 기다리는 프로세스는 wait queue에 배치됨
-   이러한 대기열은 일반적으로 PCB의 연결된 목록에서 구현됨

  
CPU 스케줄러

-   메모리에서 프로세스를 선택하여 CPU 할당
-   FIFO Queue: 선입선출
-   Priority Queue: 우선순위가 높은 프로세스 선출  
      
    

Preemptive vs Non-preemptive:

-   Non-preemptive Scheduling(비선점 스케줄링)
    -   프로세스는 CPU를 해제할 때까지 종료하거나 대기 상태로 전환하지 않고 CPU를 유지
-   Preemptive Scheduling(선제적 스케줄링)
    -   프로세스는 스케줄러에 의해 선점될 수 있음

1. 프로세스가 실행 상태에서 대기 상태로 전환되는 경우.  
2. 프로세스가 실행 상태에서 준비 상태로 전환되는 경우.  
3. 프로세스가 대기 상태에서 준비 상태로 전환되는 경우.  
4. 프로세스가 종료되는 경우.

-   1&4: Non-preemptive
-   2&3: Preemptive or Non-preemptive

Scheduling Criteria:

  
• CPU utilization: CPU 사용량  
• Throughput: 시간 단위당 완료된 프로세스 수  
• Turnaround time: 프로세스를 실행하는 데 제출 시점부터 완료 시점까지의 시간  
• Waiting time: 프로세스가 ready queue에서 대기하는 데 소요되는 시간의 합계  
• Response time: 응답을 시작하는 데 걸리는 시간  
  
Scheduling Algorithms:

-   FCFS: First-Come, First-Served(선착순)
-   SJF: Shortest Job First(최단 작업 우선)
-   SRTF: Shortest Remaining Time First(가장 짧은 남은 시간 우선)
-   RR: Round-Robin(라운드 로빈)
-   MLQ: Multi-Level Queue(다단계 대기열)
-   MLFQ: Multi-Level Feedback Queue(다단계 피드백 대기열)  
      
    
-   FCFS Scheduling
    -   선착순으로 먼저 도착한 프로세스 먼저 할당
    -   FIFO 큐로 쉽게 구현할 수 있는 가장 간단한 CPU 스케줄링 알고리즘
    -   비선점 알고리즘
-   SJF Scheduling
    -   프로세스의 다음 CPU burst의 길이가 가장 작은 프로세스 먼저 할당
    -   두 개 이상의 프로세스가 같은 CPU burst를 갖는 경우 먼저 도착한 프로세스 먼저 할당
    -   비선점 알고리즘, 선점 알고리즘 둘 다 가능하지만 보통 preemptive SJF를 SRTF라고 칭함  
          
        
-   SRTF Scheduling
    -   최단 잔여 시간 우선 할당
    -   선점적 SJF 스케줄링  
          
        
-   RR Scheduling
    -   time quantum(시간의 작은 단위, 일반적으로 10 ~ 100 밀리초)을 가지는 Preemptive FCFS.
    -   ready queue는 원형으로 처리됨
    -   CPU burst가 time quantum을 초과하면 프로세스가 선점되어 ready queue에 다시 배치됨

---

## 테스트 케이스

```
1번 : 1 (0 5 1 0)

2번 : 1 (0 5 1 1)

3번 : 3 (0 5 1 1)(0 5 1 1)(3 5 1 1)

4번 : 5 (0 200 3 3)(0 500 9 3)(0 500 20 30)(100 100 1 0)(100 500 100 3)
```

1, 2, 3번 테스트 케이스는 FCFS, SJF, RR모두 동일한 값이 나와야 함

4번 테스트 케이스는 랜덤값으로 인해 값이 달라지는 것이 정상이며 값이 나오는 추세는 비슷해야 함

- 1번 테스트 케이스
```
Scheduling Algorithm is FCFS

-----Process 1-----
A: 0, C: 5, B: 1, IO: 0
Finishing Time: 5
Turnaound Time: 5
CPU Time: 5
I/O Time: 0
Waiting Time: 0
-----Next Summary Data-----
Finishing Time: 5
CPU Utilization: 100.00%
IO Utilization: 0.00%
Throughput in processes completed per hundred time units: 20.00
Average Turnaround Time: 5.00
Average Waiting Time: 0.00
```
- 2번 테스트 케이스
```
Scheduling Algorithm is FCFS

-----Process 1-----
A: 0, C: 5, B: 1, IO: 1
Finishing Time: 9
Turnaound Time: 9
CPU Time: 5
I/O Time: 4
Waiting Time: 0
-----Next Summary Data-----
Finishing Time: 9
CPU Utilization: 55.56%
IO Utilization: 44.44%
Throughput in processes completed per hundred time units: 11.11
Average Turnaround Time: 9.00
Average Waiting Time: 0.00
```
- 3번 테스트 케이스
```
Scheduling Algorithm is FCFS

-----Process 1-----
A: 0, C: 5, B: 1, IO: 1
Finishing Time: 12
Turnaound Time: 12
CPU Time: 5
I/O Time: 4
Waiting Time: 3
-----Process 2-----
A: 0, C: 5, B: 1, IO: 1
Finishing Time: 14
Turnaound Time: 14
CPU Time: 5
I/O Time: 4
Waiting Time: 5
-----Process 3-----
A: 3, C: 5, B: 1, IO: 1
Finishing Time: 15
Turnaound Time: 12
CPU Time: 5
I/O Time: 4
Waiting Time: 3
-----Next Summary Data-----
Finishing Time: 15
CPU Utilization: 100.00%
IO Utilization: 80.00%
Throughput in processes completed per hundred time units: 20.00
Average Turnaround Time: 12.67
Average Waiting Time: 3.67
```

---

## 기타 사항

- 지도교수 : 임찬숙 교수님

- 개발환경 : <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">

## 참고

- [명령어 설명 규칙](https://technet.tmaxsoft.com/upload/download/online/jeus/pver-20170202-000001/reference-book/jeusadmin-conventions.html)
- [스케줄링 알고리즘 설명 이미지 출처 및 관련 강의 추천](https://www.inflearn.com/course/%EC%9A%B4%EC%98%81%EC%B2%B4%EC%A0%9C-%EA%B3%B5%EB%A3%A1%EC%B1%85-%EC%A0%84%EA%B3%B5%EA%B0%95%EC%9D%98/dashboard)
