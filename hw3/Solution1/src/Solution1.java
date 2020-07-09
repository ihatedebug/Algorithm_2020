import java.util.Queue;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output1.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class Solution1 {

    static final int MAX_N = 1000;
	static final int MAX_E = 100000;
	static final int Div = 100000000;  // 1억
	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int[] Answer1 = new int[MAX_N+1];
	static int[] Answer2 = new int[MAX_N+1];
    static double start1, start2;
    static double time1, time2;

    static Edge[][] adj_list;

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 시작점의 번호를 U[i], 끝점의 번호를 V[i]에, 간선의 가중치를 W[i]에 읽어들입니다.
			   (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
			stk = new StringTokenizer(br.readLine());
			N = Integer.parseInt(stk.nextToken()); E = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < E; i++) {
				U[i] = Integer.parseInt(stk.nextToken());
				V[i] = Integer.parseInt(stk.nextToken());
				W[i] = Integer.parseInt(stk.nextToken());
			}
			//make adjacency array  -> time complexity: theta(N+E)
			int [] vertexs=new int[N+1];
			for (int i = 0; i < E; i++) {
				vertexs[U[i]]++;
			}
			int max=0;
			for (int i = 1; i <= N; i++) {
				max= Math.max(max, vertexs[i]);
			}
			adj_list=new Edge[N+1][max+1];
			for(int i=0; i<N+1; i++){
				adj_list[i][0]=new Edge(vertexs[i],0);
			}
			for(int i=0; i<E; i++){
				adj_list[U[i]][vertexs[U[i]]]=new Edge(V[i],W[i]);
				vertexs[U[i]]--;
			}

            /* Problem 1-1 */
            start1 = System.currentTimeMillis();
            Answer1=BellmanFord1(1);
            time1 = (System.currentTimeMillis() - start1);

            /* Problem 1-2 */
            start2 = System.currentTimeMillis();
            Answer2=BellmanFord2(1);
            time2 = (System.currentTimeMillis() - start2);

            // output1.txt로 답안을 출력합니다.
			pw.println("#" + test_case);
            for (int i = 1; i <= N; i++) {
                pw.print(Answer1[i]);
                if (i != N)
                    pw.print(" ");
                else
                    pw.print("\n");
            }
            pw.println(time1);

            for (int i = 1; i <= N; i++) {
                pw.print(Answer2[i]);
                if (i != N)
                    pw.print(" ");
                else
                    pw.print("\n");
            }
            pw.println(time2);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
			pw.flush();
		}

		br.close();
		pw.close();
	}
	//time complexity: theta(NE)
	public static int[] BellmanFord1(int n){
		//int Answer[]=new int[MAX_N+1];
		for(int i=1; i<=N; i++){
			Answer1[i]=Integer.MAX_VALUE;
		}
		Answer1[n]=0;
		for (int i=1; i<N; i++){	//E time      -->total theta(NE)time
			for(int j=0; j<E; j++){		//N time
				if(Answer1[U[j]]!=Integer.MAX_VALUE && Answer1[U[j]]+W[j]<Answer1[V[j]]){
					Answer1[V[j]]=Answer1[U[j]]+W[j]%Div;
				}
			}
		}
		/*for(int i=1; i<=N; i++){
			Answer1[i]=Answer1[i]%Div;
		}*/
		return Answer1;
	}
	//time complexity: O(NE)
	public static int[] BellmanFord2(int n){
		//int Answer[]=new int[MAX_N+1];
		boolean []already_in=new boolean[N+1];
		Deque deque=new Deque();				//deque의 pop, push 수행시간=thata(1)
		for(int i=1; i<=N; i++){
			Answer2[i]=Integer.MAX_VALUE;
		}
		Answer2[n]=0;
		deque.push_back(n); already_in[n]=true;
		//N time to check all vertexes
		// relaxation이 일어나지 않으면 stop한다. Worst time complexity theta(NE) --> O(NE)시간을 보장.
		while(!deque.isEmpty()){
			int u=deque.pop_front(); already_in[u]=false;
			int answer_u=Answer2[u];
			for(int i=1; i<=adj_list[u][0].end; i++){
				int w=adj_list[u][i].weight;
				int v=adj_list[u][i].end;
				if(/*answer_u!=Integer.MAX_VALUE &&*/ answer_u+w<Answer2[v]){
					Answer2[v]=(answer_u+w)%Div;
					if(!already_in[v]){
						deque.push_back(v);
						already_in[v]=true;
						if(Answer2[v]<Answer2[deque.peek_front()]){
							deque.pop_back();
							deque.push_front(v);
						}
					}
				}
			}
		}
		//for(int i=1; i<=N; i++){
		//	Answer2[i]=Answer2[i]%Div;
		//}

		return Answer2;
	}
}
class Edge{
	int end;
	int weight;
	Edge(int v, int w){
		end=v;
		weight=w;
	}
}
class Node{
	int data;
	Node llink;
	Node rlink;
	Node(){
		llink=null;
		rlink=null;
	}
	Node(int n){
		data=n;
		llink=null;
		rlink=null;
	}
}
class Deque{
	Node front;
	Node rear;
	Deque(){
		front=null;
		rear=null;
	}
	boolean isEmpty(){
		return front==null;
	}
	void push_front(int n) {
		Node node = new Node(n);
		if (isEmpty()) {
			front = node;
			rear = node;
			node.rlink = null;
			node.llink = null;
		} else {
			node.rlink = front;
			node.llink = null;
			front.llink = node;
			front = node;
		}
	}
	public int pop_front(){
		if(isEmpty()){
			return -1;
		}
		else{
			int n = front.data;
			if(front.rlink==null){
				front = null;
				rear = null;
			}else{
				front = front.rlink;
				front.llink = null;
			}
			return n;
		}
	}

	public int pop_back(){
			int n = rear.data;
			if(rear.llink==null){
				front = null;
				rear = null;
			}
			else{
				rear = rear.llink;
				rear.rlink = null;
			}
			return n;
	}

	public int peek_front(){
		return front.data;
	}

	public void push_back(int n){
		Node node =  new Node(n);
		if(isEmpty()){
			front = node;
			rear = node;
			node.rlink = null;
			node.llink = null;
		}else{
			node.llink = rear;
			node.rlink = null;
			rear.rlink = node;
			rear = node;
		}
	}


}













