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
simulate된 process의 수 등을 출력하고, 그 다 음 각 process에 대해 다음과 같은 내용을 출력한다.

- (A, C, B, IO)
- Finishing Time(그 process가 끝난 시간)
- Turnaround Time (= finishing time - A)
- CPU time (즉, 그 process가 running state에 있었던 시간의 총합)
- I/O time (즉, 그 process가 blocked state에 있었던 시간의 총합)
- Waiting time(즉, 그 process가 ready state에 있었던 시간의 총합) (마지막 3가지의 시간의 합은 Turnaround time과 같아야 함)

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

---

## 사용 방법

-   jdk 11 버전으로 작성되었으며 추가적인 라이브러리 설치는 필요하지 않음
-   Client.java : 테스트 케이스를 키보드로 입력, 스케줄링 알고리즘은 코드에서 수정하여야 함
-   Sched.java : 다음과 같은 코드를 terminal에 입력해야 함

```
cd 디렉터리명/Project-CPU-Scheduler/src/
javac Sched.java
java Sched 텍스트파일명.txt 알고리즘명 quantum
```

---

## 클래스 다이어그램

<img width="854" alt="스크린샷 2023-06-28 오후 4 52 23" src="https://github.com/juintination/Project-CPU-Scheduler/assets/89019601/e65035a7-a3b8-4242-89e6-30eb88ff3e59">

---

## 스케줄링 알고리즘 설명

- 추후 추가 예정

---

## 기타 사항

- 지도교수 : 임찬숙 교수님

- 개발환경 : <img src="https://img.shields.io/badge/IntelliJ IDEA-000000?style=for-the-badge&logo=IntelliJ IDEA&logoColor=white"> <img src="https://img.shields.io/badge/Visual Studio Code-007ACC?style=for-the-badge&logo=Visual Studio Code&logoColor=white">

## 참고

- 추후 추가 예정
