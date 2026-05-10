import SwiftUI
import app

struct ContentView: View {

    var body: some View {
        ComposeView().ignoresSafeArea(.keyboard)
    }
}

struct ComposeView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainIOSKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
