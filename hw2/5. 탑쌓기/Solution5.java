import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

class Solution5 {
	static final int max_n = 1000;

	static int n, H;
	static int[] h = new int[max_n], d = new int[max_n-1];
	static int Answer;

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
			Answer=0;

			stk = new StringTokenizer(br.readLine());
			n = Integer.parseInt(stk.nextToken()); H = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n; i++) {				//n번 수행
				h[i] = Integer.parseInt(stk.nextToken());
			}
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < n-1; i++) {				//n번 수행
				d[i] = Integer.parseInt(stk.nextToken());
			}

			/////////////////////////////////////////////////////////////////////////////////////////////
			int[][] height= new int [2][H+1];
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
			height[0][h[0]]=height[1][h[1]]=1;
			if(h[0]+h[1]-d[0]<=H){
				height[1][(h[0]+h[1]-d[0])]++;
			}

			for(int i=2; i<n; i++){					//n번 수행
				int temp[]=new int [H+1];
				for(int j=1; j<=H-h[i]; j++){		//기껏해야 H번 수행
					if(height[0][j]>0) {
						temp[j + h[i]]+=height[0][j];
					}
				}
				temp[h[i]]++;

				for(int j=1; j<=H-h[i]+d[i-1]; j++){		//기껏해야 H번 수행
					if(height[1][j]>0) {
						temp[j - d[i - 1] + h[i]]+=height[1][j];
					}
				}


				for(int j=1; j<=H; j++){			//H번 수행
					height[0][j]+=height[1][j];
					height[1][j]=temp[j];
					height[0][j]=height[0][j]%1000000;
					height[1][j]=height[1][j]%1000000;
				}

			}

			for(int i=0; i<2; i++){
				for(int j=1; j<=H; j++){			//2*H번 수행
					Answer+=height[i][j];
					Answer=Answer%1000000;
				}
			}
			//Complexity: theta(nH). i번째 블록 바로 직전 블록이 있는 경우와 없는 경우 나눠서 수행 각각의 경우 H번 수행. i는 n번(0~n-1) 수행
			/////////////////////////////////////////////////////////////////////////////////////////////

			// output5.txt로 답안을 출력합니다.
			pw.println("#" + test_case + " " + Answer);
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
}

