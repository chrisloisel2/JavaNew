public class SimpleReactiveExample {

    public static void main(String[] args) throws Exception {

		try (var publisher = new SubmissionPublisher<Integer>()) {

			// Création du processor
			SimpleProcessor processor = new SimpleProcessor();

			// Abonnement du processor au publisher
			publisher.subscribe(processor);

			// Abonnement du subscriber au processor
			processor.subscribe(new SimpleSubscriber());

			// Publication d'une valeur
			publisher.submit(5);
			publisher.submit(10);
			publisher.submit(15);
		}

            System.out.println("Publisher -> Fin du flux");
        }
    }

    static class SimpleSubscriber implements Flow.Subscriber<Integer> {

        private Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            System.out.println("Subscriber -> Abonné !");
            subscription.request(1);
        }

        @Override
        public void onNext(Integer item) {
            System.out.println("Subscriber -> Reçu : " + item);
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("Subscriber -> Erreur : " + throwable.getMessage());
        }

        @Override
        public void onComplete() {
            System.out.println("Subscriber -> Flux terminé !");
        }
    }
}
