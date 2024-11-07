import java.util.Random;
import java.util.Arrays;

public class AlgoritmosDeOrdenacao {
    // Define os tamanhos dos vetores a serem testados
    private static final int[] TAMANHOS = {1000, 10000, 100000, 500000, 1000000};
    // Define o número de rodadas para cada teste
    private static final int RODADAS = 5;

    public static void main(String[] args) {
        // Itera sobre cada tamanho de vetor definido
        for (int tamanho : TAMANHOS) {
            System.out.println("Tamanho do vetor: " + tamanho);
            // Variáveis para armazenar os resultados acumulados
            long tempoTotalMerge = 0, tempoTotalGnome = 0;
            long trocasTotaisMerge = 0, trocasTotaisGnome = 0;
            long iteracoesTotaisMerge = 0, iteracoesTotaisGnome = 0;

            // Executa o número de rodadas definido
            for (int rodada = 0; rodada < RODADAS; rodada++) {
                // Gera um vetor aleatório e uma cópia dele
                int[] vetor = gerarVetorAleatorio(tamanho, rodada);
                int[] vetorCopia = Arrays.copyOf(vetor, vetor.length);

                // Executa os algoritmos de ordenação e obtém os resultados
                Resultado resultadoMerge = ordenacaoMerge(vetor);
                Resultado resultadoGnome = ordenacaoGnome(vetorCopia);

                // Acumula os resultados
                tempoTotalMerge += resultadoMerge.tempo;
                trocasTotaisMerge += resultadoMerge.trocas;
                iteracoesTotaisMerge += resultadoMerge.iteracoes;

                tempoTotalGnome += resultadoGnome.tempo;
                trocasTotaisGnome += resultadoGnome.trocas;
                iteracoesTotaisGnome += resultadoGnome.iteracoes;
            }

            // Calcula e imprime as médias dos resultados
            System.out.println("Merge Sort - Tempo Médio: " + (tempoTotalMerge / RODADAS) + "ms, Trocas Médias: " + (trocasTotaisMerge / RODADAS) + ", Iterações Médias: " + (iteracoesTotaisMerge / RODADAS));
            System.out.println("Gnome Sort - Tempo Médio: " + (tempoTotalGnome / RODADAS) + "ms, Trocas Médias: " + (trocasTotaisGnome / RODADAS) + ", Iterações Médias: " + (iteracoesTotaisGnome / RODADAS));
            System.out.println();
        }
    }

    // gera um vetor de inteiros aleatórios( As seeds)
    private static int[] gerarVetorAleatorio(int tamanho, int semente) {
        Random aleatorio = new Random(semente);
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1000000);
        }
        return vetor;
    }

    // implementação do Merge Sort
    private static Resultado ordenacaoMerge(int[] vetor) {
        long tempoInicio = System.currentTimeMillis();
        long trocas = 0;
        long iteracoes = 0;

        // Chama o metodo auxiliar recursivo
        Resultado resultado = auxiliarOrdenacaoMerge(vetor, 0, vetor.length - 1);
        trocas += resultado.trocas;
        iteracoes += resultado.iteracoes;

        long tempoFim = System.currentTimeMillis();
        return new Resultado(tempoFim - tempoInicio, trocas, iteracoes);
    }

    // Metodo auxiliar recursivo do Merge Sort
    private static Resultado auxiliarOrdenacaoMerge(int[] vetor, int esquerda, int direita) {
        long trocas = 0;
        long iteracoes = 0;

        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;

            // Divide o vetor e ordena recursivamente
            Resultado resultadoEsquerda = auxiliarOrdenacaoMerge(vetor, esquerda, meio);
            Resultado resultadoDireita = auxiliarOrdenacaoMerge(vetor, meio + 1, direita);

            // Mescla as duas metades ordenadas
            Resultado resultadoMescla = mesclar(vetor, esquerda, meio, direita);

            // Acumula os resultados
            trocas += resultadoEsquerda.trocas + resultadoDireita.trocas + resultadoMescla.trocas;
            iteracoes += resultadoEsquerda.iteracoes + resultadoDireita.iteracoes + resultadoMescla.iteracoes;
        }

        return new Resultado(0, trocas, iteracoes);
    }

    // Metodo para mesclar duas sub-listas ordenadas
    private static Resultado mesclar(int[] vetor, int esquerda, int meio, int direita) {
        int n1 = meio - esquerda + 1;
        int n2 = direita - meio;

        // Aqui ele define o lado direito e esquerdo e adiciona dados a eles
        int[] E = new int[n1];
        int[] D = new int[n2];

        //valores da esquerda
        for (int i = 0; i < n1; ++i)
            E[i] = vetor[esquerda + i];

        //valores da direita
        for (int j = 0; j < n2; ++j)
            D[j] = vetor[meio + 1 + j];

        // Mescla/junta os dois lados
        int i = 0, j = 0, k = esquerda;
        long trocas = 0, iteracoes = 0;

        while (i < n1 && j < n2) {
            iteracoes++;
            if (E[i] <= D[j]) {
                vetor[k] = E[i];
                i++;
            } else {
                vetor[k] = D[j];
                j++;
                trocas++;
            }
            k++;
        }

        // Copia os elementos restantes de E, se houver
        while (i < n1) {
            iteracoes++;
            vetor[k] = E[i];
            i++;
            k++;
        }

        // Copia os elementos restantes de D, se houver
        while (j < n2) {
            iteracoes++;
            vetor[k] = D[j];
            j++;
            k++;
        }

        return new Resultado(0, trocas, iteracoes);
    }

    // Implementação do Gnome Sort
    // O gnome vai tentar ordenar o algoritimo em sua devida ordem
    private static Resultado ordenacaoGnome(int[] vetor) {
        long tempoInicio = System.currentTimeMillis();
        long trocas = 0;
        long iteracoes = 0;

        int index = 0;
        while (index < vetor.length) {
            iteracoes++;
            if (index == 0 || vetor[index] >= vetor[index - 1]) {
                index++;
            } else {
                // Troca os elementos se estiverem fora de ordem
                int temp = vetor[index];
                vetor[index] = vetor[index - 1];
                vetor[index - 1] = temp;
                index--;
                trocas++;
            }
        }

        long tempoFim = System.currentTimeMillis();
        return new Resultado(tempoFim - tempoInicio, trocas, iteracoes);
    }

    // Classe interna para armazenar os resultados de cada execução
    private static class Resultado {
        long tempo;
        long trocas;
        long iteracoes;

        Resultado(long tempo, long trocas, long iteracoes) {
            this.tempo = tempo;
            this.trocas = trocas;
            this.iteracoes = iteracoes;
        }
    }
}